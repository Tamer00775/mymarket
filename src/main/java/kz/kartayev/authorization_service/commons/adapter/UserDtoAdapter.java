package kz.kartayev.authorization_service.commons.adapter;

import kz.kartayev.authorization_service.commons.dto.CardDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCardInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDtoAdapter {
  public static UserDto toDto(User user) {
    UserDto userDto = UserDto.builder()
            .id(user.getId())
            .age(user.getAge())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .isActive(user.getIsActive())
            .build();


    if (user.getUserCardInfoList() != null && !user.getUserCardInfoList().isEmpty()) {
      userDto.setUserCards(user.getUserCardInfoList().stream()
              .map(UserDtoAdapter::toCardDto).collect(Collectors.toList()));
    }

    return userDto;
  }

  public static CardDto toCardDto(UserCardInfo userCardInfo) {
    return CardDto.builder()
            .code(userCardInfo.getCardNumber())
            .isActive(userCardInfo.getIsActive())
            .expDate(userCardInfo.getExpDate())
            .cash(userCardInfo.getCash() != null ? userCardInfo.getCash() :  0)
            .id(userCardInfo.getId())
            .build();
  }

}
