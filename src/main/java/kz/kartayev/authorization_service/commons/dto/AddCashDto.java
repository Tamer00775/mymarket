package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.Wallet;
import lombok.Data;

@Data
public class AddCashDto {
  private Double cash;
  private Wallet currencyType;
}
