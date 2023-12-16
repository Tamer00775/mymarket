package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class OrderDto {
  private Long id;
  private LocalDateTime createdDate;
  private LocalDateTime deliveryDate;
  private UserDto buyerDto;
  private Boolean isPayed;
  private String deliveryAddress;
}
