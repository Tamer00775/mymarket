package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.ProductBuyDto;
import kz.kartayev.authorization_service.commons.dto.ProductCreateDto;
import kz.kartayev.authorization_service.commons.dto.ProductDto;
import kz.kartayev.authorization_service.commons.dto.ProductSearchDto;
import kz.kartayev.authorization_service.service.ProductCommonService;
import kz.kartayev.authorization_service.util.error.ErrorResponse;
import kz.kartayev.authorization_service.util.error.ErrorUtil;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
@Slf4j
@ApiModel(description = "Продукты в сервисе")
public class ProductController {
  private final ProductCommonService productCommonService;

  @GetMapping
  @ApiOperation("Получение всех продуктов без фильтра с пагинацией")
  public ResponseEntity<Page<ProductDto>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "createdDate") String sort) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    return ResponseEntity.ok(productCommonService.findAll(pageable));
  }
  @GetMapping("/search")
  @ApiOperation("Получение всех продуктов c фильтром и пагинацией")
  public ResponseEntity<Page<ProductDto>> search(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "createdDate") String sort,
                                                 @RequestBody ProductSearchDto productSearchDto) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
    return ResponseEntity.ok(productCommonService.findAll(productSearchDto, pageable));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
  @ApiOperation("Создание продукта")
  public ResponseEntity<ProductDto> create(@RequestBody @Valid ProductCreateDto productDto,
                                           BindingResult bindingResult) throws Exception{
    if (bindingResult.hasErrors()) {
      ErrorUtil.getFieldErrors(bindingResult);
    }
    return ResponseEntity.ok(productCommonService.save(productDto));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'CUSTOMER')")
  @ApiOperation("Изменение продукта")
  public ResponseEntity<ProductDto> update(@PathVariable("id") Long id, @RequestBody ProductCreateDto
          productCreateDto) {
    return ResponseEntity.ok(productCommonService.update(id, productCreateDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'CUSTOMER')")
  @ApiOperation("Удаление продукта")
  public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
    log.info("start delete product by id {}", id);
    productCommonService.delete(id);
    log.info("finish deleting product");
    return ResponseEntity.ok(HttpStatus.OK);
  }


  @PostMapping("/{id}/buy")
  @ApiOperation("Покупка продукта текущим пользователем")
  public void buy(@PathVariable Long id, @RequestBody ProductBuyDto productBuyDto) {
    productCommonService.buy(id, productBuyDto);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> errorHandler(FLCException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis(), e.getCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
