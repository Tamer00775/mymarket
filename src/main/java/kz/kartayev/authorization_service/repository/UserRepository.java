package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findUserByUsername(String username);

  @Query("SELECT ur.user FROM UserRole ur WHERE ur.userRoles LIKE 'ADMIN' or ur.userRoles LIKE 'SUPER_ADMIN'")
  List<User> findUsersByRoleAdmin();

  Optional<User> findUserByEmail(String email);
}
