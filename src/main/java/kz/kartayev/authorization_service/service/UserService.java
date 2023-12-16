package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.commons.adapter.UserDtoAdapter;
import kz.kartayev.authorization_service.commons.dto.RegistrationDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.commons.enums.UserRoles;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserCredentials;
import kz.kartayev.authorization_service.entity.UserRole;
import kz.kartayev.authorization_service.repository.CardRepository;
import kz.kartayev.authorization_service.repository.UserCredentialsRepository;
import kz.kartayev.authorization_service.repository.UserRepository;
import kz.kartayev.authorization_service.repository.UserRoleRepository;
import kz.kartayev.authorization_service.util.registration.RegistrationValidation;
import kz.kartayev.authorization_service.util.security.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final List<RegistrationValidation> registrationValidationList;
  private final PasswordEncoder passwordEncoder;
  private final UserCredentialsRepository userCredentialsRepository;
  private final UserRoleRepository userRoleRepository;
  private final EmailService emailService;

  public UserDto findById(Long id) {
    log.info("start user searching by id {}", id);
    Optional<User> optUser = userRepository.findById(id);
    if(optUser.isEmpty()) {
      throw new IllegalArgumentException("user-not-found");
    }
    User user = optUser.get();
    return UserDtoAdapter.toDto(user);
  }

  public Optional<User> findByIdEntity(Long id) {
    return userRepository.findById(id);
  }

  public User findByUsername(String login) {
    Optional<User> user =  userRepository.findUserByUsername(login);
    return user.orElseThrow();
  }

  public User findExecutor() {
    List<User> users = userRepository.findUsersByRoleAdmin();
    // TODO : fix to get executor by category_type
    return users.get(0);
  }

  @Transactional
  public UserDto createUser(RegistrationDto registrationDto) {
    log.info("start creating user with email {}", registrationDto.getEmail());

    registrationValidationList.forEach(validate -> {
      validate.validation(registrationDto);
    });
    User user = new User();
    user.setAge(registrationDto.getAge());
    user.setEmail(registrationDto.getEmail());
    user.setIsActive(true);
    user.setFirstName(registrationDto.getFirstName());
    user.setLastName(registrationDto.getLastName());
    user.setUsername(registrationDto.getEmail().substring(0, registrationDto.getEmail().indexOf("@")));

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
    userCredentials.setUpdatedAt(LocalDateTime.now());
    userCredentials.setPassExpired(false);
    userCredentials.setPassExpDate(LocalDateTime.now().plus(90, ChronoUnit.DAYS));
    userCredentials.setUser(user);
    user.setUserCredentials(userCredentials);

    UserRole userRole = new UserRole();
    userRole.setUserRoles(UserRoles.USER);
    userRole.setStartDate(LocalDateTime.now());
    userRole.setEndDate(LocalDateTime.now().plus(12, ChronoUnit.MONTHS));
    userRole.setUser(user);

    userRepository.save(user);
    userCredentialsRepository.save(userCredentials);
    userRoleRepository.save(userRole);
    emailService.sendMessage(user.getEmail(), "Приложение MyMarket", "Вы успешно прошли регистрацию!");
    return UserDtoAdapter.toDto(user);
  }

  public UserDto getCurrentUser() {
    User user = SecurityUtils.getCurrentUser();
    if(user != null) {
      return UserDtoAdapter.toDto(user);
    }
    return null;
  }

  public User getCurrentUserEntity() {
    User user = SecurityUtils.getCurrentUser();
    return user;
  }

  public List<UserDto> findAll() {
    return userRepository.findAll().stream().map(UserDtoAdapter::toDto).collect(Collectors.toList());
  }

  public void save(User user) {
    userRepository.save(user);
  }

  public void saveRole(UserRole userRole) {
    userRoleRepository.save(userRole);
  }

  public Optional<User> findByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  public List<UserRole> findAllUserRole(User user) {
    return userRoleRepository.findAllByUser(user);
  }
}
