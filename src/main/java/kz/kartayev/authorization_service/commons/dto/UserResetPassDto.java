package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

@Data
public class UserResetPassDto {
  private String login;
  private String tempPass;
  private String newPass;
  private String oldPass;
}
