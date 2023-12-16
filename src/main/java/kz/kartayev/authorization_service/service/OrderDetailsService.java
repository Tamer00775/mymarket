package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.OrderDetailsDtoAdapter;
import kz.kartayev.authorization_service.commons.dto.OrderDetailsDto;
import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import kz.kartayev.authorization_service.entity.OrderDetails;
import kz.kartayev.authorization_service.entity.UserCustomer;
import kz.kartayev.authorization_service.repository.OrderDetailsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderDetailsService {
  private final OrderDetailsRepository orderDetailsRepository;

  public List<OrderDetailsDto> findByCustomer(UserCustomer userCustomer) {
    return orderDetailsRepository.findAllByUserCustomerAndDeliveryStatus(
            userCustomer, DeliveryStatus.QUEUED).stream()
            .map(OrderDetailsDtoAdapter::toDto).collect(Collectors.toList());
  }

  public Optional<OrderDetails> findById(Long id) {
    return orderDetailsRepository.findById(id);
  }
  public OrderDetails findByCode(String code) {
    return orderDetailsRepository.findOrderDetailsByDeliveryCodeAndDeliveryStatus(code, DeliveryStatus.IN_PROCESS);
  }

  public void save(OrderDetails orderDetails) {
    orderDetailsRepository.save(orderDetails);
  }
}
