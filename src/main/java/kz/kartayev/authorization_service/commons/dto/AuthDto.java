package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

@Data
public class AuthDto {
  private String username;
  private String password;
}
