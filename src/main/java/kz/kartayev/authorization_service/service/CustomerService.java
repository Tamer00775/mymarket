package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.SellerCommentAdapter;
import kz.kartayev.authorization_service.commons.dto.CustomerDto;
import kz.kartayev.authorization_service.commons.dto.OrderCustomerDto;
import kz.kartayev.authorization_service.commons.dto.OrderDetailsDto;
import kz.kartayev.authorization_service.commons.dto.ProductDto;
import kz.kartayev.authorization_service.commons.dto.SellerCommentDto;
import kz.kartayev.authorization_service.commons.enums.DeliveryStatus;
import kz.kartayev.authorization_service.commons.enums.TransactionStatusHistory;
import kz.kartayev.authorization_service.entity.Order;
import kz.kartayev.authorization_service.entity.OrderDetails;
import kz.kartayev.authorization_service.entity.Product;
import kz.kartayev.authorization_service.entity.SellerComment;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCardInfo;
import kz.kartayev.authorization_service.entity.UserCustomer;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;
import kz.kartayev.authorization_service.repository.OrderDetailsRepository;
import kz.kartayev.authorization_service.repository.UserCustomerRepository;
import kz.kartayev.authorization_service.util.error.FLCException;
import kz.kartayev.authorization_service.util.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService {
  private final UserCustomerRepository userCustomerRepository;
  private final OrderDetailsService orderDetailsService;
  private final OrderService orderService;
  private final EmailService emailService;
  private UserTransactionHistoryService userTransactionHistoryService;
  private CardService cardService;

  public Optional<UserCustomer> findCustomerByName(String name) {
    return userCustomerRepository.findUserCustomerByName(name);
  }

  public Optional<UserCustomer> findCustomerByUser(User user) {
    return userCustomerRepository.findUserCustomerByUser(user);
  }

  public List<OrderDetailsDto> findAllMyOrders() {
    User user = SecurityUtils.getCurrentUser();
    Optional<UserCustomer> userCustomer = userCustomerRepository.findUserCustomerByUser(user);
    return orderDetailsService.findByCustomer(userCustomer.get());
  }

  @Transactional
  public void order(Long id, OrderCustomerDto orderCustomerDto) {
    Optional<OrderDetails> orderDetailsOptional = orderDetailsService.findById(id);
    if (orderDetailsOptional.isPresent()) {
      OrderDetails orderDetails = orderDetailsOptional.get();
      Order order = orderDetails.getOrder();
      UserTransactionHistory userTransactionHistory = order.getTransactionHistory();
      if (orderCustomerDto.getDeliveryStatus().equals(DeliveryStatus.IN_PROCESS)) {
        orderDetails.setDeliveryStatus(DeliveryStatus.IN_PROCESS);
        Random random = new Random();
        String code = String.valueOf(1000 + random.nextInt(100));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(orderCustomerDto.getDeliveryDate(), dateTimeFormatter);
        order.setDeliveryDate(localDate.atStartOfDay());
        orderDetails.setDeliveryCode(code);
        orderService.save(order);
        sendSuccessMessage(order.getBuyer().getEmail(), userTransactionHistory.getId(), localDate, code);
      }
      else {
        orderDetails.setDeliveryStatus(DeliveryStatus.CANCELED);
        if (order.getIsPayed()) {
          if (userTransactionHistory.getTransactionStatus().equals(TransactionStatusHistory.SUCCESS)) {
            userTransactionHistory.setComment(orderCustomerDto.getComment());
            userTransactionHistory.setTransactionStatus(TransactionStatusHistory.RETURNED);
            User user = order.getBuyer();
            Optional<UserCardInfo> cardInfos = user.getUserCardInfoList().stream().filter(a ->
                    a.getCardNumber().equals(userTransactionHistory.getCardNum())).findFirst();
            if (cardInfos.isPresent()) {
              UserCardInfo userCardInfo = cardInfos.get();
              userCardInfo.setCash(userTransactionHistory.getSum() + userCardInfo.getCash());
              cardService.save(userCardInfo);
            }
            userTransactionHistoryService.save(userTransactionHistory);
          }
          orderService.save(order);
          sendCanceledMessage(order.getBuyer().getEmail(), orderCustomerDto.getComment(), userTransactionHistory.getId());
        }
      }
    }
  }

  public void setStatusDelivered(String code) {
    OrderDetails orderDetails = orderDetailsService.findByCode(code);
    if (orderDetails != null) {
      orderDetails.setDeliveryStatus(DeliveryStatus.DELIVERED);
      orderDetails.setDeliveryCode(null);
      orderDetailsService.save(orderDetails);
      deliveredMessage(orderDetails.getOrder().getBuyer().getEmail());
    }
    else {
      throw new FLCException("DELIVERY.ERROR", "Вы ввели код неправильно!", "");
    }
  }

  public void deliveredMessage(String to) {
    String header = "Информация о доставке!";
    String text = "Ваш заказ доставлен успешно! Благодарим за то, что выбираете наш сервис!";
    sendMessage(to, header, text);
  }
  public void sendCanceledMessage(String to, String comment, Long transactionId) {
    String header = "Информация о заказе №" + transactionId;
    String text = "Доставка отклонена. Ваши деньги вернуться в течении 24 часов. Если деньги не поступят на Вашу карту"
            + " просим обратиться в Службу Технической Поддержки MyMarket! Причина отказа от продавца: " + comment;
    sendMessage(to, header, text);

  }

  public void sendSuccessMessage(String to, Long transactionId, LocalDate localDate, String code) {
    String header = "Информация о заказе №" + transactionId;
    String text = "Ваша заявка принята! Будет доставлено до " + localDate + "\n Код доставки: " + code +
            "\nНАПОМИНАНИЕ:КОД ДОСТАВКИ ПОДТВЕРЖДАЕТ ЧТО ВЫ ВЛАДЕЛЕЦ ТОВАРА!";
    sendMessage(to, header, text);
  }
  public void sendMessage(String to, String header, String text) {
    emailService.sendMessage(to, header, text);
  }

  public Optional<UserCustomer> findById(Long id) {
    return userCustomerRepository.findById(id);
  }

  public CustomerDto findCustomerById(Long id) {
    Optional<UserCustomer> userCustomer = findById(id);
    if (userCustomer.isPresent()) {
      UserCustomer customer = userCustomer.get();
      CustomerDto customerDto = CustomerDto.builder()
              .id(customer.getId())
              .customerName(customer.getName())
              .rating(customer.getAvgRate())
              .build();
      List<SellerComment> sellerComments = customer.getSellerComments();
      customerDto.setCommentDtoList(sellerComments.stream().map(SellerCommentAdapter::toDto).collect(Collectors.toList()));
      customerDto.setRatingNums(sellerComments.size());
      return customerDto;
    }
    throw new FLCException("", "Продавец не найден!", "");
  }

  public void save(UserCustomer userCustomer) {
    userCustomerRepository.save(userCustomer);
  }
}
