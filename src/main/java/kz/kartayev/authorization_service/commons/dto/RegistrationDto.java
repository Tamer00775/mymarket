package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

@Data
public class RegistrationDto {
  private String firstName;
  private String lastName;
  private Integer age;
  private String email;
  private String password;
}
