package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ProductCreateDto {
  private String productName;
  private PriceDto priceDto;
  private CategoryDto categoryDto;
  private Integer quantity;
  private String description;
  private CustomerDto customerDto;
}
