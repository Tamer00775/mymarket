package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.CardDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.commons.dto.UserTransactionHistoryDto;
import kz.kartayev.authorization_service.service.CardService;
import kz.kartayev.authorization_service.service.UserService;
import kz.kartayev.authorization_service.service.UserTransactionHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserTransactionHistoryService userTransactionHistoryService;

  @GetMapping("{id}")
  @PreAuthorize("hasRole('USER')")
  @ApiOperation("Получение пользователя по id")
  public ResponseEntity<UserDto> findById(@PathVariable("id") Long id) {
    return ResponseEntity.ok().body(userService.findById(id));
  }

  @GetMapping("/transactions")
  @ApiOperation("Все транзакции текущего пользователя")
  public ResponseEntity<List<UserTransactionHistoryDto>> findAll() {
    return ResponseEntity.ok(userTransactionHistoryService.findAllTransactionByUser(
            userService.getCurrentUserEntity()));
  }
}
