package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.CustomerRequest;
import kz.kartayev.authorization_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Long> {
  List<CustomerRequest> findAllByAdmin(User admin);
}
