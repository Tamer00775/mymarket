package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.dto.ProductSearchDto;
import kz.kartayev.authorization_service.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpecificationService {

  public Specification<Product> productSpecification(ProductSearchDto productSearchDto) {
    return Specification.where(((root, query, criteriaBuilder) -> {
      List<javax.persistence.criteria.Predicate> predicates = new ArrayList<>();

      if(productSearchDto.getProductName() != null) {
        predicates.add(criteriaBuilder.like(root.get("productName"), "%" +
                productSearchDto.getProductName() + "%"));
      }

      if(productSearchDto.getDescription() != null) {
        predicates.add(criteriaBuilder.like(root.get("description"), "%" +
                productSearchDto.getDescription() + "%"));
      }

      if(productSearchDto.getFrom() != null) {
        predicates.add(criteriaBuilder.greaterThan(root.get("createdDate"),
                productSearchDto.getFrom()));
      }

      if(productSearchDto.getTo() != null) {
        predicates.add(criteriaBuilder.lessThan(root.get("createdDate"),
                productSearchDto.getTo()));
      }

      if (productSearchDto.getCategoryDto() != null) {
        predicates.add(criteriaBuilder.equal(root.get("category").get("id"),
                productSearchDto.getCategoryDto().getId()));
      }

      if(productSearchDto.getPriceDto() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price").get("price"),
                productSearchDto.getPriceDto().getPrice()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }));
  }
}
