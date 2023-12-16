package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoleDto {
  private String code;
}
