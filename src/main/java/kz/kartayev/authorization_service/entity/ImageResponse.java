package kz.kartayev.authorization_service.entity;

import lombok.Data;

import java.util.List;

@Data
public class ImageResponse {
  private List<byte[]> images;
}
