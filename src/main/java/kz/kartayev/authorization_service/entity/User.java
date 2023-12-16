package kz.kartayev.authorization_service.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="_user")
@SQLDelete(sql = "update _user set is_active=false where id=?")
@Getter
@Setter
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name="first_name")
  @NotNull
  private String firstName;
  @Column(name="last_name")
  @NotNull
  private String lastName;
  @Column(name="email")
  @NotNull
  private String email;
  @NotNull
  @Min(value = 17)
  private Integer age;
  @Column(name="is_active")
  private Boolean isActive;
  @Column(name="username")
  private String username;
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserCredentials userCredentials;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @Fetch(FetchMode.SELECT)
  private List<UserRole> userRoles;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<UserCardInfo> userCardInfoList;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<UserTransactionHistory> userTransactionHistories;
}
