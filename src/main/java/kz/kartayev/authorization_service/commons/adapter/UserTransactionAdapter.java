package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.UserTransactionHistoryDto;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;

public class UserTransactionAdapter {
  public static UserTransactionHistoryDto toDto(UserTransactionHistory userTransactionHistory) {
    return UserTransactionHistoryDto.builder()
            .id(userTransactionHistory.getId())
            .sum(userTransactionHistory.getSum())
            .transactionType(userTransactionHistory.getTransactionType())
            .transactionStatus(userTransactionHistory.getTransactionStatus())
            .build();
  }
}
