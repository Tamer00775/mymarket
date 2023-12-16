package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.dto.UserResetPassDto;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCredentials;
import kz.kartayev.authorization_service.repository.UserCredentialsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class PasswordService {
  private final UserCredentialsRepository userCredentialsRepository;
  private final EmailService emailService;
  private final UserService userService;

  private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  @Transactional
  public boolean resetPassword(UserResetPassDto userResetPassDto, String login) {
    User user = userService.findByUsername(login);
    UserCredentials userCredentials = user.getUserCredentials();
    boolean res = false;
    if(userResetPassDto.getTempPass().equals(userCredentials.getTemporaryPassword())) {
      log.info("start change password user {}", login);
      userCredentials.setPassword(userResetPassDto.getNewPass());
      userCredentials.setPassExpDate(LocalDateTime.now().plus(30, ChronoUnit.DAYS));
      userCredentials.setPassExpired(false);
      userCredentials.setTemporaryPassword(null);
      res = true;
    }
    return res;
  }

  @Scheduled(cron = "0 0 9 * * *")
  @Transactional
  public void checkExpiredPassDate() {
    log.info("start scheduled checking for expired password");
    List<UserCredentials> userCredentialsRepositoryList = userCredentialsRepository
            .findAllByPassExpDateBefore(LocalDateTime.now());
    List<UserCredentials> userToBlockList = userCredentialsRepository.findAllByPassExpDateBefore(LocalDateTime.now().minus(
            15, ChronoUnit.DAYS));
    userToBlockList.forEach(user -> {
      User userToBlock = user.getUser();
      userToBlock.setIsActive(false);
    });

    userCredentialsRepositoryList.forEach(userCredentials -> {
      User user = userCredentials.getUser();
      String temp = generateCode();
      userCredentials.setUpdatedAt(LocalDateTime.now());
      userCredentials.setPassExpired(false);
      userCredentials.setTemporaryPassword(null);

      userCredentialsRepository.save(userCredentials);
      // TODO: сделать ссылку для перехода в страницу сброса пароля

      String subject = "Напоминание о истечении срока пароля!";
      String text = "Уважаемый " + user.getFirstName() + " " + user.getLastName() +
              "! Напоминаем Вам о том, что у Вас истек пароль. Вам необходимо его обновить. Через три дня Ваш аккаунт "
              + " деактивируется. Ваш код для сброса пароля: " + temp;
      emailService.sendMessage(user.getEmail(), subject, text);
    });
  }

  private String generateCode() {
    Random random = new Random();
    StringBuilder code = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      int index = random.nextInt(CHARACTERS.length());
      code.append(CHARACTERS.charAt(index));
    }

    return code.toString();
  }
}
