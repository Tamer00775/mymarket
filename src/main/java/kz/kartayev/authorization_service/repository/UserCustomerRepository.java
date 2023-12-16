package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCustomerRepository extends JpaRepository<UserCustomer, Long> {
  Optional<UserCustomer> findUserCustomerByName(String name);
  Optional<UserCustomer> findUserCustomerByUser(User user);
}
