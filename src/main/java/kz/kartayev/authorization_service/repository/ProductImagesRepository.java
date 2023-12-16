package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

  List<ProductImages> findAllByProduct(Product product);

  void deleteByProduct(Product product);
}
