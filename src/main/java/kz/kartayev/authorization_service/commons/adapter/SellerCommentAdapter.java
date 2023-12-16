package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.SellerCommentDto;
import kz.kartayev.authorization_service.entity.SellerComment;

public class SellerCommentAdapter {
  public static SellerCommentDto toDto(SellerComment sellerComment) {
    return SellerCommentDto.builder()
            .id(sellerComment.getId())
            .comment(sellerComment.getCommentText())
            .author(sellerComment.getAuthor())
            .createdDate(sellerComment.getCreatedDate())
            .rate(sellerComment.getRating())
            .build();
  }
}
