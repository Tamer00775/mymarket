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
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="seller_comment")
@Data
public class SellerComment {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="created_date")
  private LocalDateTime createdDate;

  @Column(name="comment")
  private String commentText;

  @Column(name="rate")
  private Integer rating;

  @Column(name="fullname")
  private String author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id")
  private UserCustomer userCustomer;
}
