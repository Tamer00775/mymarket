package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  List<UserRole> findAllByUser(User user);
}
