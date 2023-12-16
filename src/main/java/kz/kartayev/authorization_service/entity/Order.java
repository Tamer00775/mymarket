package kz.kartayev.authorization_service.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="orders")
@Data
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="created_date")
  private LocalDateTime createdDate;

  @Column(name="delivery_date")
  private LocalDateTime deliveryDate;

  @Column(name = "delivery_address")
  private String deliveryAddress;

  @Column(name="is_payed")
  private Boolean isPayed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "buyer_id")
  private User buyer;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
  private List<OrderDetails> orderDetailsList;

  @OneToOne
  @JoinColumn(name = "transaction_id", unique = true)
  private UserTransactionHistory transactionHistory;
}
