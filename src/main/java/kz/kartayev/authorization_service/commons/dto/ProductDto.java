package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
public class ProductDto {
  private Long id;
  private String productName;
  private LocalDateTime createdDate;
  private PriceDto priceDto;
  @NotNull
  private CategoryDto categoryDto;
  private Integer quantity;
  private String description;
  private CustomerDto customerDto;
}
