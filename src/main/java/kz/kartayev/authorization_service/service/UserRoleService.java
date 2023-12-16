package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserRole;
import kz.kartayev.authorization_service.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class UserRoleService {
  private final UserRoleRepository userRoleRepository;

  @Transactional
  public void save(UserRoles userRoles, User user) {
    UserRole userRole = new UserRole();
    userRole.setUserRoles(userRoles);
    userRole.setUser(user);
    userRole.setStartDate(LocalDateTime.now());
    userRole.setEndDate(LocalDateTime.now().plus(12, ChronoUnit.MONTHS));
    userRoleRepository.save(userRole);
  }
}
