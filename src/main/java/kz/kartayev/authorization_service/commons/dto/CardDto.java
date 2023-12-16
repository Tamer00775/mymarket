package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
public class CardDto {
  private Long id;
  @Size(max = 16, min = 16, message = "the card number must have 16 characters")
  private String code;
  private Boolean isActive;
  private LocalDate expDate;
  private Double cash;
}
