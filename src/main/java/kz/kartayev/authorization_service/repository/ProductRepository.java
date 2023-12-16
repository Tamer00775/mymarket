package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
  List<Product> findAllByUserCustomer(UserCustomer userCustomer);
}
