package kz.kartayev.authorization_service.commons.dto;

import kz.kartayev.authorization_service.commons.enums.Wallet;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PriceDto {
  private Long id;
  private Long price;
  private Boolean isSale;
  private Wallet currencyType;
}
