package kz.kartayev.authorization_service.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="user_customer")
@Data
public class UserCustomer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Long id;

  @Column(name="customer_name", unique = true)
  private String name;

  @Column(name="license_num", unique = true)
  private String licenseNum;

  @Column(name="start_date")
  private LocalDateTime startDate;

  @Column(name="end_date")
  private LocalDateTime endDate;

  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;

  @OneToMany(mappedBy = "userCustomer", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<Product> products;

  @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY)
  private List<SellerComment> sellerComments;

  @Column(name="avg_rate")
  private Double avgRate;
}
