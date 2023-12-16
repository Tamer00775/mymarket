package kz.kartayev.authorization_service.entity;

import kz.kartayev.authorization_service.commons.enums.Wallet;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="price")
public class Price {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="created_date")
  private LocalDateTime updatedAt;

  @Column(name="price")
  private Long price;

  @Column(name="is_sale")
  private Boolean isSale;

  @Enumerated
  @Column(name="currency_type")
  private Wallet currencyType;
}
