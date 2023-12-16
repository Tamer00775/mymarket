package kz.kartayev.authorization_service.commons.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductBuyDto {
  private Long cardId;
  private Integer quantity;
  @NotNull(message = "Вы обязательно должны указать адрес доставки: ")
  private String deliveryAddress;
  @NotNull(message = "Укажите способ оплаты")
  private Boolean isPayed;
}
