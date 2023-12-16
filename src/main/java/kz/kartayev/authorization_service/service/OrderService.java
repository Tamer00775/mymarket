package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.dto.ProductBuyDto;
import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import kz.kartayev.authorization_service.entity.Order;
import kz.kartayev.authorization_service.entity.OrderDetails;
import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;
import kz.kartayev.authorization_service.repository.OrderDetailsRepository;
import kz.kartayev.authorization_service.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderDetailsRepository orderDetailsRepository;

  public Order save(ProductBuyDto productBuyDto, User buyer, Product product, UserTransactionHistory userTransactionHistory) {
    Order order = new Order();
    order.setIsPayed(productBuyDto.getIsPayed());
    order.setBuyer(buyer);
    order.setCreatedDate(LocalDateTime.now());
    order.setDeliveryAddress(productBuyDto.getDeliveryAddress());
    order.setTransactionHistory(userTransactionHistory);
    orderRepository.save(order);

    OrderDetails orderDetails = new OrderDetails();
    orderDetails.setOrder(order);
    orderDetails.setDeliveryStatus(DeliveryStatus.QUEUED);
    orderDetails.setProduct(product);
    orderDetails.setUserCustomer(product.getUserCustomer());
    orderDetails.setPrice(Double.valueOf(product.getPrice().getPrice()));
    orderDetailsRepository.save(orderDetails);
    return order;
  }

  public void save(Order order) {
    orderRepository.save(order);
  }
}
