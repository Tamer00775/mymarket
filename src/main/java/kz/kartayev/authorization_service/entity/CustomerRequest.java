package kz.kartayev.authorization_service.entity;

import kz.kartayev.authorization_service.commons.enums.Status;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer_request")
@Data
public class CustomerRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="request_id")
  private Long requestId;

  @OneToOne
  @JoinColumn(name="executor_id")
  private User admin;

  @ManyToOne
  @JoinColumn(name = "customer_id") // Имя столбца, который является внешним ключом
  private User customer;

  @Column(name="customer_name")
  private String customerName;

  @Column(name="license_num")
  private String licenseNum;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;
}
