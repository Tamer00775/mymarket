package kz.kartayev.authorization_service.util.security;

import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.security.PersonDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
  public static User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object obj = authentication.getPrincipal();
    PersonDetails personDetails = (PersonDetails) obj;
    User user = personDetails.getUser();
    return user;
  }
}
