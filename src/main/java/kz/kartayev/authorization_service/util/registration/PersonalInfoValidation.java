package kz.kartayev.authorization_service.util.registration;

import kz.kartayev.authorization_service.commons.dto.RegistrationDto;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonalInfoValidation implements RegistrationValidation{
  @Override
  public void validation(RegistrationDto registrationDto) {
    log.info("start validation personal info {}", registrationDto);
    if (registrationDto.getAge() == null) {
      throw new FLCException("age must be filled");
    }
    if (registrationDto.getLastName() == null) {
      throw new FLCException("lastname must be filled");
    }
    if (registrationDto.getFirstName() == null) {
      throw new FLCException("firstname must be filled");
    }

    String firstName = registrationDto.getFirstName();
    String lastName = registrationDto.getLastName();
    if (firstName.isEmpty() || lastName.isEmpty() || firstName.isBlank() || lastName.isBlank()) {
      throw new FLCException("lastname or firstname is empty");
    }
    if (registrationDto.getAge() < 17) {
      throw new FLCException("your age must higher than 17");
    }
  }
}
