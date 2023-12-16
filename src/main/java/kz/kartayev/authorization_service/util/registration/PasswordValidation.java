package kz.kartayev.authorization_service.util.registration;

import kz.kartayev.authorization_service.commons.dto.RegistrationDto;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordValidation implements RegistrationValidation{
  @Override
  public void validation(RegistrationDto registrationDto) {
    log.info("start validation password");
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    String pass = registrationDto.getPassword();
    if(pass.isEmpty()) {
      throw new FLCException("your pass must be filled");
    }
    if(!pass.matches(regex)) {
      throw new FLCException("your pass must have one upper case, and digits, and greater or equal"
              + " than 8 characters");
    }
  }
}
