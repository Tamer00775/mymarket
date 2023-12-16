package kz.kartayev.authorization_service.commons.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerDto {
  private Long id;
  private String customerName;
  private Double rating;
  private List<SellerCommentDto> commentDtoList;
  private Integer ratingNums;
}
