package kz.kartayev.authorization_service.entity;

import kz.kartayev.authorization_service.commons.enums.TransactionStatusHistory;
import kz.kartayev.authorization_service.commons.enums.TransactionType;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name="user_transaction_history")
@Entity
@Data
public class UserTransactionHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  @Column(name = "transaction_status")
  @Enumerated(EnumType.STRING)
  private TransactionStatusHistory transactionStatus;

  @Column(name="sum")
  private Double sum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "transaction_type")
  @Enumerated(EnumType.STRING)
  private TransactionType transactionType;

  @Column(name="comment")
  private String comment;

  @Column(name="card_num")
  private String cardNum;
}
