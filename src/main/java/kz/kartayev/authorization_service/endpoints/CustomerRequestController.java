package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.CustomerRequestCreateDto;
import kz.kartayev.authorization_service.commons.dto.CustomerRequestDto;
import kz.kartayev.authorization_service.service.CustomerRequestService;
import kz.kartayev.authorization_service.util.error.ErrorResponse;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request")
@AllArgsConstructor
@ApiModel(description = "Заявки пользователей на продажу")
public class CustomerRequestController {
  private final CustomerRequestService customerRequestService;

  @PostMapping("/create")
  @ApiOperation("Создание заявки на продажу товара")
  public ResponseEntity<HttpStatus> createCustomerRequest(@RequestBody CustomerRequestCreateDto customerRequestCreateDto)
          throws FLCException {
    customerRequestService.createRequest(customerRequestCreateDto);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> errorHandler(FLCException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis(), e.getCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
