package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.CustomerRequestDto;
import kz.kartayev.authorization_service.commons.dto.RejectDto;
import kz.kartayev.authorization_service.service.CustomerRequestService;
import kz.kartayev.authorization_service.util.error.ErrorResponse;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/requests")
@ApiModel(description = "Все заявки")
public class RequestController {
  private final CustomerRequestService customerRequestService;

  @GetMapping("/my")
  @ApiOperation("Заявки которые должен одобрить текущий зарегистрированный админ")
  public ResponseEntity<List<CustomerRequestDto>> requests() {
    return ResponseEntity.ok(customerRequestService.findAdminApproveRequests());
  }
  @GetMapping
  // TODO : доступно только для супер админа
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @ApiOperation("Список всех заявок")
  public ResponseEntity<List<CustomerRequestDto>> findAll() {
    return ResponseEntity.ok(customerRequestService.findAll());
  }
  @PostMapping("/approve/{id}")
  // TODO : доступно только Админу и Суперадмину
  @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation("Одобрение заявки админом")
  public ResponseEntity<HttpStatus> approveRequestById(@PathVariable("id") Long id) throws FLCException {
    customerRequestService.approveRequest(id);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @PostMapping("/reject/{id}")
  @ApiOperation("Отклонение заявки админом")
  // TODO : доступно только Админу и Суперадмину
  public ResponseEntity<HttpStatus> rejectRequestById(@PathVariable("id") Long id, @RequestBody RejectDto reason) throws FLCException {
    customerRequestService.rejectRequest(id, reason);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> errorHandler(FLCException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis(), e.getCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
