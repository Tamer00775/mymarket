package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.CategoryDto;
import kz.kartayev.authorization_service.commons.dto.CustomerDto;
import kz.kartayev.authorization_service.commons.dto.PriceDto;
import kz.kartayev.authorization_service.commons.dto.ProductDto;
import kz.kartayev.authorization_service.entity.Product;

public class ProductAdapter {
  public static ProductDto toDto(Product product) {
    PriceDto priceDto = PriceDto.builder().
            price(product.getPrice().getPrice())
            .id(product.getPrice().getId())
            .currencyType(product.getPrice().getCurrencyType())
            .isSale(product.getPrice().getIsSale()).build();

    System.out.println(priceDto.getCurrencyType());
    CategoryDto categoryDto = CategoryDto.builder()
            .categoryName(product.getCategory().getCategoryName())
            .id(product.getCategory().getId())
            .build();

    CustomerDto customerDto = CustomerDto.builder()
            .customerName(product.getUserCustomer().getName())
            .id(product.getUserCustomer().getId())
            .rating(product.getUserCustomer().getAvgRate() == null ? 0 : product.getUserCustomer().getAvgRate())
            .ratingNums(product.getUserCustomer().getSellerComments().size())
            .build();

    return ProductDto.builder()
            .id(product.getId())
            .productName(product.getProductName())
            .description(product.getDescription())
            .createdDate(product.getCreatedDate())
            .quantity(product.getQuantity())
            .priceDto(priceDto)
            .categoryDto(categoryDto)
            .quantity(product.getQuantity())
            .customerDto(customerDto)
            .build();
  }
}
