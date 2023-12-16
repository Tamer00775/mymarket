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
@Table(name="product")
@Data
public class Product {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne
  @JoinColumn(name = "price_id", unique = true)
  private Price price;
  @ManyToOne
  @JoinColumn(name = "customer_id")
  private UserCustomer userCustomer;
  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
  @Column(name="created_date")
  private LocalDateTime createdDate;
  @Column(name="product_name")
  private String productName;
  @Column(name="description")
  private String description;
  @Column(name="quantity")
  private Integer quantity;

  @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
  List<ProductImages> productImagesList;

}
