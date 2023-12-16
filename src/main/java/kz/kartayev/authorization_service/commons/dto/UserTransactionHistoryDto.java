package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.TransactionStatusHistory;
import kz.kartayev.authorization_service.commons.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserTransactionHistoryDto {
  private Double sum;
  private Long id;
  private TransactionType transactionType;
  private TransactionStatusHistory transactionStatus;
}
