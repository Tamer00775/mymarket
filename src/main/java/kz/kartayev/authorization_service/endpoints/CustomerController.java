package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.CustomerDto;
import kz.kartayev.authorization_service.commons.dto.DeliveryDto;
import kz.kartayev.authorization_service.commons.dto.OrderCustomerDto;
import kz.kartayev.authorization_service.commons.dto.OrderDetailsDto;
import kz.kartayev.authorization_service.commons.dto.ProductDto;
import kz.kartayev.authorization_service.service.CustomerService;
import kz.kartayev.authorization_service.service.ProductCommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@ApiModel(description = "Информация о продавце! UPD: спутал слово customer and seller")
public class CustomerController {
  private CustomerService customerService;
  private ProductCommonService productCommonService;

  @GetMapping("/{id}")
  public ResponseEntity<CustomerDto> findCustomerById(@PathVariable Long id) {
    return ResponseEntity.ok(customerService.findCustomerById(id));
  }

  @GetMapping("/my")
  @ApiOperation("Список продуктов текущего продавца")
  public ResponseEntity<List<ProductDto>> findAllMyProduct() {
    return ResponseEntity.ok(productCommonService.findMyProducts());
  }

  @GetMapping("/orders")
  @ApiOperation("Список заказов текущего продавца")
  public ResponseEntity<List<OrderDetailsDto>> findAllMyOrders() {
    return ResponseEntity.ok(customerService.findAllMyOrders());
  }

  @PostMapping("/orders/{id}")
  @ApiOperation("Согласие или отказ на доставку. Возврат денег пользователю")
  public ResponseEntity<HttpStatus> getOrder(@PathVariable Long id, @RequestBody OrderCustomerDto orderCustomerDto) {
    customerService.order(id, orderCustomerDto);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/order-delivery")
  @ApiOperation("Продавец указывает статус доставление товара. ")
  public ResponseEntity<HttpStatus> setStatusDelivered(@RequestBody DeliveryDto deliveryDto) {
    customerService.setStatusDelivered(deliveryDto.getCode());
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
