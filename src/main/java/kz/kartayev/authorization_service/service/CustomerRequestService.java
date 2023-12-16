package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.CustomerRequestAdapter;
import kz.kartayev.authorization_service.commons.adapter.UserDtoAdapter;
import kz.kartayev.authorization_service.commons.dto.CustomerRequestCreateDto;
import kz.kartayev.authorization_service.commons.dto.CustomerRequestDto;
import kz.kartayev.authorization_service.commons.dto.RejectDto;
import kz.kartayev.authorization_service.commons.enums.Status;
import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.CustomerRequest;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCustomer;
import kz.kartayev.authorization_service.repository.CustomerRequestRepository;
import kz.kartayev.authorization_service.repository.UserCustomerRepository;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerRequestService {
  private final CustomerRequestRepository customerRequestRepository;
  private final UserService userService;
  private final UserCustomerRepository userCustomerRepository;
  private final EmailService emailService;

  private final UserRoleService userRoleService;

  @Transactional
  public void approveRequest(Long id) throws FLCException {
    Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findById(id);
    if (optionalCustomerRequest.isPresent()) {
      CustomerRequest customerRequest = optionalCustomerRequest.get();
      UserCustomer userCustomer = new UserCustomer();
      userCustomer.setStartDate(LocalDateTime.now());
      userCustomer.setEndDate(LocalDateTime.now().plus(12, ChronoUnit.MONTHS));
      userCustomer.setUser(customerRequest.getCustomer());
      userCustomer.setLicenseNum(customerRequest.getLicenseNum());
      userCustomer.setName(customerRequest.getCustomerName());
      userCustomerRepository.save(userCustomer);
      userRoleService.save(UserRoles.CUSTOMER, customerRequest.getCustomer());
      customerRequest.setStatus(Status.APPROVED);
      customerRequestRepository.save(customerRequest);
      String text = "Уважаемый " + userCustomer.getUser().getFirstName() + ", Ваша заявка была одобрена админом " +
              customerRequest.getAdmin().getFirstName() + " " + customerRequest.getAdmin().getLastName();
      sendMessage(userCustomer.getUser().getEmail(), "MyMarket Заявка №" + customerRequest.getRequestId(), text);
    }
    else {
      handleError();
    }
  }

  @Transactional
  public void rejectRequest(Long id, RejectDto reason) throws FLCException {
    Optional<CustomerRequest> optionalCustomerRequest = customerRequestRepository.findById(id);
    if (optionalCustomerRequest.isPresent()) {
      CustomerRequest customerRequest = optionalCustomerRequest.get();
      if (customerRequest.getStatus().equals(Status.REJECTED)) {
        throw new FLCException("R1", "заявка уже отклонена", "");
      }
      customerRequest.setStatus(Status.REJECTED);
      String text = "Ваша заявка №"+ customerRequest.getRequestId() + " в MyMarket отказано. Причина:"
              + reason.getReason() + "\nАдмин: " + customerRequest.getAdmin().getFirstName() + " " + customerRequest.getAdmin().getLastName();
      sendMessage(customerRequest.getCustomer().getEmail(), "MyMarket Заявка №" + customerRequest.getRequestId(), text);
      return;
    }
    handleError();
  }

  private void handleError() throws FLCException {
    throw new FLCException("C-1", "Заявка не найдена!", "");
  }
  @Transactional
  public void createRequest(CustomerRequestCreateDto customerRequestCreateDto) throws FLCException {
    validateAttributes(customerRequestCreateDto);
    CustomerRequest customerRequest = new CustomerRequest();
    User executor = userService.findExecutor();
    if (executor == null) {
      throw new FLCException("001", "admin-not-found", "");
    }
    customerRequest.setAdmin(executor);
    customerRequest.setCustomer(userService.getCurrentUserEntity());
    customerRequest.setLicenseNum(customerRequestCreateDto.getCustomerLicense());
    customerRequest.setCustomerName(customerRequestCreateDto.getCustomerName());
    customerRequest.setStatus(Status.CREATED);
    customerRequestRepository.save(customerRequest);
    String text = "Вам поступила заявка на одобрение продажи на сайте. \n"
            + "Номер заявки: " + customerRequest.getRequestId() + "\n"
            + "Заявитель: " + customerRequest.getCustomer().getFirstName() + " " + customerRequest.getCustomer().getLastName()
            + "\n С Уважением, Служба Поддержки MyMarket";

    String subject = "Заявка на одобрение продаж";
    sendMessage(customerRequest.getAdmin().getEmail(), subject, text);
  }

  public List<CustomerRequestDto> findAdminApproveRequests() {
    User admin = userService.getCurrentUserEntity();
    return customerRequestRepository.findAllByAdmin(admin).stream()
            .map(CustomerRequestAdapter::toDto).collect(Collectors.toList());
  }

  public List<CustomerRequestDto> findAll() {
    return customerRequestRepository.findAll().stream().map(CustomerRequestAdapter::toDto).collect(Collectors.toList());
  }

  private void sendMessage(String emailCustomer, String subject, String text) {
    emailService.sendMessage(emailCustomer, subject, text);
  }

  private void validateAttributes(CustomerRequestCreateDto customerRequestCreateDto) throws FLCException {
    if (customerRequestCreateDto.getCustomerName() == null ||
            customerRequestCreateDto.getCustomerName().isEmpty() ||
    customerRequestCreateDto.getCustomerName().isBlank()) {
      throw new FLCException("C-1", "Название вашего магазина не должно быть пустым.", "");
    }
    if (customerRequestCreateDto.getCustomerLicense() == null && customerRequestCreateDto.getCustomerLicense().isEmpty() ||
    customerRequestCreateDto.getCustomerLicense().isBlank()) {
      throw new FLCException("C-1", "Поле лицензия обязательна к заполнению.", "");
    }
    if (!customerRequestCreateDto.getCustomerLicense().matches("\\d{6}")) {
      throw new FLCException("C-1", "Лицензия должна состоять из шести цифр", "");

    }
  }
}
