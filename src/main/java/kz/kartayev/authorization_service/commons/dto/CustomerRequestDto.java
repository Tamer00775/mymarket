package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRequestDto {
  private Long id;
  private UserDto customer;
  private String licenseNum;
  private String customerName;
  private Status status;
}
