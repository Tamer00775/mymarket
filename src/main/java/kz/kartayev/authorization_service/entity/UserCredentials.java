package kz.kartayev.authorization_service.entity;

import kz.kartayev.authorization_service.commons.enums.UserRoles;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name="user_credential")
@Data
public class UserCredentials {
  @Id
  @Column(name="id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name="password")
  private String password;
  @Column(name="pass_exp")
  private Boolean passExpired;
  @Column(name="pass_exp_date")
  private LocalDateTime passExpDate;
  @Column(name="update_at")
  private LocalDateTime updatedAt;
  @OneToOne
  @JoinColumn(name = "user_id", unique = true)
  private User user;
  @Column(name="temp_pass")
  private String temporaryPassword;
}
