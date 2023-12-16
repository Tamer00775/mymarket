package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserDto {
  private Long id;
  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private Boolean isActive;
  private List<CardDto> userCards;
  private List<RoleDto> roleDtos;
}
