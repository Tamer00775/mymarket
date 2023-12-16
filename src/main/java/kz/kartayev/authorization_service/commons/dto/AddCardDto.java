package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Builder
@Data
public class AddCardDto {
  private String expDate;
  @Size(max = 16, min = 16, message = "the card number must have 16 characters")
  private String code;
}
