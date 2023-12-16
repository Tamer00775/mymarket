package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

@Data
public class UserCreateDto {
  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private Boolean isActive;
  private String password;
}
