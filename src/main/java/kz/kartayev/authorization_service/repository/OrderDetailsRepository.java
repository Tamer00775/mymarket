package kz.kartayev.authorization_service.repository;

import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import kz.kartayev.authorization_service.entity.OrderDetails;
import kz.kartayev.authorization_service.entity.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
  List<OrderDetails> findAllByUserCustomerAndDeliveryStatus(UserCustomer userCustomer, DeliveryStatus deliveryStatus);

  OrderDetails findOrderDetailsByDeliveryCodeAndDeliveryStatus(String deliveryCode, DeliveryStatus deliveryStatus);
}
