package kz.kartayev.authorization_service.entity;

import kz.kartayev.authorization_service.commons.enums.UserRoles;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@Data
public class UserRole {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="role")
  @Enumerated(EnumType.STRING)
  private UserRoles userRoles;

  @Column(name="start_date")
  private LocalDateTime startDate;

  @Column(name="end_date")
  private LocalDateTime endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private User user;
}
