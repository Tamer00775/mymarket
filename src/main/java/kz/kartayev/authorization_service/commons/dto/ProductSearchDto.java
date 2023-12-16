package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductSearchDto {
  private LocalDateTime from;
  private LocalDateTime to;
  private String productName;
  private PriceDto priceDto;
  private CategoryDto categoryDto;
  private Integer quantity;
  private String description;
}
