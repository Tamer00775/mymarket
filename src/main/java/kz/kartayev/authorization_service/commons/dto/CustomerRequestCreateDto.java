package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequestCreateDto {
  private String customerName;
  private String customerLicense;
}
