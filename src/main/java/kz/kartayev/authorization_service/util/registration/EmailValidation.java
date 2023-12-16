package kz.kartayev.authorization_service.util.registration;

import kz.kartayev.authorization_service.commons.dto.RegistrationDto;
import kz.kartayev.authorization_service.repository.UserRepository;
import kz.kartayev.authorization_service.service.UserService;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Component
public class EmailValidation implements RegistrationValidation{
  private final UserRepository userRepository;
  @Override
  public void validation(RegistrationDto registrationDto) {
    log.info("start validation email");
    String email = registrationDto.getEmail();
    String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    if(!email.matches(regex)) {
      throw new FLCException("Email not satisfied email format");
    }
    if (userRepository.findUserByEmail(registrationDto.getEmail()).isPresent()) {
      throw new FLCException("email привязан другому пользователю");
    }
  }
}
