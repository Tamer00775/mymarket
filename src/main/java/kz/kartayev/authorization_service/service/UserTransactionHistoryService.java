package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.UserTransactionAdapter;
import kz.kartayev.authorization_service.commons.dto.UserTransactionHistoryDto;
import kz.kartayev.authorization_service.commons.enums.TransactionStatusHistory;
import kz.kartayev.authorization_service.commons.enums.TransactionType;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCardInfo;
import kz.kartayev.authorization_service.entity.UserTransactionHistory;
import kz.kartayev.authorization_service.repository.CardRepository;
import kz.kartayev.authorization_service.repository.UserTransactionHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserTransactionHistoryService {
  private final UserTransactionHistoryRepository userTransactionHistoryRepository;
  private final CardRepository cardRepository;

  @Transactional
  public void save(UserTransactionHistory userTransactionHistory) {
    userTransactionHistoryRepository.save(userTransactionHistory);
  }

  @Transactional
  public UserTransactionHistory save(Double totalCash, TransactionType type, User user, UserCardInfo userCardInfo) {
    UserTransactionHistory userTransactionHistory = new UserTransactionHistory();
    try {
      userTransactionHistory.setUser(user);
      userTransactionHistory.setSum(totalCash);
      userTransactionHistory.setTransactionStatus(TransactionStatusHistory.SUCCESS);
      userTransactionHistory.setTransactionType(type);
      userTransactionHistory.setCardNum(userCardInfo.getCardNumber());
      if (type.equals(TransactionType.CASH)) {
        userCardInfo.setCash(totalCash + (userCardInfo.getCash() == null ? 0 : userCardInfo.getCash()));
      }
      else {
        if (userCardInfo.getCash() < totalCash) {
          throw new IllegalArgumentException("Не хватает средств");
        }
        userCardInfo.setCash(userCardInfo.getCash() - totalCash);
      }
      cardRepository.save(userCardInfo);
      userTransactionHistoryRepository.save(userTransactionHistory);
    } catch (Exception e) {
      userTransactionHistory.setTransactionStatus(TransactionStatusHistory.FAILURE);
      userTransactionHistory.setComment(e.getMessage());
      userTransactionHistoryRepository.save(userTransactionHistory);
    }
    return userTransactionHistory;
  }


  public List<UserTransactionHistoryDto> findAllTransactionByUser(User user) {
    return userTransactionHistoryRepository.findAllByUser(user).stream()
            .map(UserTransactionAdapter::toDto).collect(Collectors.toList());
  }
}

