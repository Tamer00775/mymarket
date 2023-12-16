package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
  List<UserCredentials> findAllByPassExpDateBefore(LocalDateTime localDateTime);
}
