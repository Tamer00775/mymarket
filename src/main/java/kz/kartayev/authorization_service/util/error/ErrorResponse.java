package kz.kartayev.authorization_service.util.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private String message;
  private long timestamp;
  private String code;
}
