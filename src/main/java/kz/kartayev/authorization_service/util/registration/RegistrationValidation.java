package kz.kartayev.authorization_service.util.registration;

import kz.kartayev.authorization_service.commons.dto.RegistrationDto;

public interface RegistrationValidation {
  void validation(RegistrationDto registrationDto);
}
