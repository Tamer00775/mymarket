package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class OrderCustomerDto {
  private DeliveryStatus deliveryStatus;
  private String comment;
  private String deliveryDate;
}
