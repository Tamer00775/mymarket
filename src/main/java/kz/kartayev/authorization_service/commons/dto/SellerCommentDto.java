package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class SellerCommentDto {
  private Long id;
  private LocalDateTime createdDate;
  private String author;
  private String comment;
  private Integer rate;
}
