package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import kz.kartayev.authorization_service.entity.UserCustomer;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderDetailsDto {
  private Long id;
  private OrderDto orderDto;
  private ProductDto productDto;
  private CustomerDto customerDto;
  private DeliveryStatus deliveryStatus;
  private Double price;
  private String deliveryCode;
  private Integer quantity;
}
