package kz.kartayev.authorization_service.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {
  private Long id;
  private byte[] imageData;
}
