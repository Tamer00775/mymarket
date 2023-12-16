package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.UserDtoAdapter;
import kz.kartayev.authorization_service.commons.dto.AddCardDto;
import kz.kartayev.authorization_service.commons.dto.AddCashDto;
import kz.kartayev.authorization_service.commons.dto.CardDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.commons.enums.TransactionType;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCardInfo;
import kz.kartayev.authorization_service.repository.CardRepository;
import kz.kartayev.authorization_service.repository.UserTransactionHistoryRepository;
import kz.kartayev.authorization_service.util.error.FLCException;
import kz.kartayev.authorization_service.util.security.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CardService {
  private final CardRepository cardRepository;
  private final UserTransactionHistoryService userTransactionHistoryService;
  public List<CardDto> myCards() throws Exception{
    User user = SecurityUtils.getCurrentUser();
    return user.getUserCardInfoList().stream().map(UserDtoAdapter::toCardDto)
            .collect(Collectors.toList());
  }
  @Transactional
  public void addCard(AddCardDto cardDto) throws FLCException {
    if (cardRepository.findUserCardInfoByCardNumber(cardDto.getCode()).isPresent()) {
      throw new FLCException("A1", "Эта карта уже привязана другому человеку", "");
    }
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/yy");
    YearMonth yearMonth = YearMonth.parse("08/25", dateTimeFormatter);
    LocalDate localDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);

    User user = SecurityUtils.getCurrentUser();
    UserCardInfo userCardInfo = new UserCardInfo();
    userCardInfo.setCardNumber(cardDto.getCode());
    userCardInfo.setExpDate(localDate);
    // TODO : add scheduled to remove not active card
    userCardInfo.setIsActive(true);
    userCardInfo.setUser(user);
    cardRepository.save(userCardInfo);
  }
  @Transactional
  public UserDto updateCard(Long id, CardDto cardDto) {
    Optional<UserCardInfo> optionalUserCardInfo = cardRepository.findById(id);
    if(optionalUserCardInfo.isEmpty()) {
      throw new IllegalArgumentException("this card does not exists");
    }
    UserCardInfo userCardInfo = optionalUserCardInfo.get();
    if (!userCardInfo.getCardNumber().equals(cardDto.getCode())) {
      userCardInfo.setCardNumber(cardDto.getCode());
    }
    cardRepository.save(userCardInfo);
    return UserDtoAdapter.toDto(SecurityUtils.getCurrentUser());
  }
  @Transactional
  public void deleteCard(Long id) {
    cardRepository.deleteById(id);
  }


  @Transactional
  public void addCash(Long id, AddCashDto addCashDto) {
    // TODO возвращает 400
    if (addCashDto.getCash() < 0 || addCashDto.getCash() > 100000) {
      throw new FLCException("C2", "Вы можете произвести транзакцию не больше 100 тысяч", "");
    }
    log.info("start add cash addCashDto {}", addCashDto);
    Optional<UserCardInfo> optUserCardInfo = cardRepository.findById(id);
    if (optUserCardInfo.isPresent()) {
      UserCardInfo userCardInfo = optUserCardInfo.get();
      userTransactionHistoryService.save(addCashDto.getCash(),
              TransactionType.CASH, userCardInfo.getUser(), userCardInfo);
      return;
    }
    throw new IllegalArgumentException(String.format("card not found id %s", id));
  }

  public void save(UserCardInfo userCardInfo) {
    cardRepository.save(userCardInfo);
  }

  public Optional<UserCardInfo> findById(Long id) {
    return cardRepository.findById(id);
  }
}
