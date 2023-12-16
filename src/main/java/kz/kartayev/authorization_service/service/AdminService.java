package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCredentials;
import kz.kartayev.authorization_service.entity.UserRole;
import kz.kartayev.authorization_service.repository.UserCredentialsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminService {
  private final UserService userService;
  private final UserCredentialsRepository userCredentialsRepository;

  @Transactional
  public void assignAdmin(Long userId) {
    Optional<User> optUser = userService.findByIdEntity(userId);
    if (optUser.isPresent()) {
      User user = optUser.get();
      UserCredentials userCredential = user.getUserCredentials();

      user.setUserCredentials(userCredential);
      List<UserRole> userRoles = user.getUserRoles();

      UserRole userRole = new UserRole();
      userRole.setUser(user);
      userRole.setUserRoles(UserRoles.ADMIN);
      userRoles.add(userRole);
      userRole.setStartDate(LocalDateTime.now());
      userRole.setEndDate(LocalDateTime.now().plus(12, ChronoUnit.MONTHS));

      user.setUserRoles(userRoles);

      userService.saveRole(userRole);
      userCredentialsRepository.save(userCredential);
      userService.save(user);
    }
  }

  @Transactional
  public void blockUser(Long userId) {
    Optional<User> optUser = userService.findByIdEntity(userId);
    if (optUser.isPresent()) {
      User user = optUser.get();
      user.setIsActive(false);
      userService.save(user);
    }
  }

  public List<UserDto> findAll() {
    return userService.findAll();
  }
}
