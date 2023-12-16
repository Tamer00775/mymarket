package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.AddCardDto;
import kz.kartayev.authorization_service.commons.dto.AddCashDto;
import kz.kartayev.authorization_service.commons.dto.CardDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.service.CardService;
import kz.kartayev.authorization_service.util.error.ErrorResponse;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/card")
@ApiModel(description = "Банковские карты пользователя")
public class CardController {
  private final CardService cardService;
  @PostMapping("/add")
  @ApiOperation("Добавление карты пользователю")
  public ResponseEntity<HttpStatus> addCard(@RequestBody AddCardDto cardDto) throws FLCException {
    cardService.addCard(cardDto);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @PostMapping("/{id}")
  @ApiOperation("Добавление денег в карту. Имитация")
  public ResponseEntity<HttpStatus> addCash(@PathVariable Long id, @RequestBody AddCashDto addCashDto) {
    cardService.addCash(id, addCashDto);
    return ResponseEntity.ok(HttpStatus.ACCEPTED);
  }

  @PutMapping("/{id}")
  @ApiOperation(("Изменение данных в карте"))
  @PreAuthorize("hasRole('USER')")
  public ResponseEntity<UserDto> update(@PathVariable("id") Long id, @RequestBody CardDto cardDto) {
    return ResponseEntity.ok(cardService.updateCard(id, cardDto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
    cardService.deleteCard(id);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @GetMapping("/my")
  @ApiOperation("Получение карты авторизованного пользователя")
  public ResponseEntity<List<CardDto>> myCards() throws Exception {
    return ResponseEntity.ok(cardService.myCards());
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> errorHandler(FLCException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis(), e.getCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
