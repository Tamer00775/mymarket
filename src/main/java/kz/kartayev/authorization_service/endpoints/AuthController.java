package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.AuthDto;
import kz.kartayev.authorization_service.commons.dto.RegistrationDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.commons.dto.UserResetPassDto;
import kz.kartayev.authorization_service.security.JwtUtil;
import kz.kartayev.authorization_service.service.PasswordService;
import kz.kartayev.authorization_service.service.PersonDetailsService;
import kz.kartayev.authorization_service.service.UserService;
import kz.kartayev.authorization_service.util.error.ErrorResponse;
import kz.kartayev.authorization_service.util.error.FLCException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final PersonDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  private final PasswordService passwordService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  @ApiOperation("Логин пользователя")
  public Map<String, String> login(@RequestBody AuthDto authDto) {
    try {
      UserDetails userDetails = userDetailsService.loadUserByUsername(authDto.getUsername());
      if(userDetails.isCredentialsNonExpired()) {
        return Map.of("password", "Ваш пароль истек! Восстановить можете с помощью ссылки, "
                + "которое было отправлено в Вашу почту!");
      }
      if(userDetails.isAccountNonExpired()) {
        return Map.of("password", "Ваш аккаунт заблокирован, так как был неактивный! Можете разблокировать аккаунт с "
                + "помощью ссылки, которое было направлено на Вашу почту от нашего сервиса.");
      }
      if (passwordEncoder.matches(authDto.getPassword(), userDetails.getPassword())) {
        String token = jwtUtil.generateToken(authDto.getUsername());
        return Map.of("token", token);
      }
    }
    catch (Exception e) {
      return Map.of("message", e.getMessage());
    }
    return Map.of("message", "incorrect credentials!");
  }

  @PostMapping(value = "/registration")
  @ApiOperation("Регистрация пользователя")
  public ResponseEntity<UserDto> register(@RequestBody RegistrationDto registrationDto) {
    return ResponseEntity.ok(userService.createUser(registrationDto));
  }

  @GetMapping("/current")
  @ApiOperation("Получение текущего зарегистрированного пользователя")
  public ResponseEntity<UserDto> currentUser() {
    return ResponseEntity.ok(userService.getCurrentUser());
  }

  @PostMapping(value = "/reset-password")
  @ApiOperation("Сброс истекшего пароля")
  public Map<String, String> login(@RequestBody UserResetPassDto userResetPassDto) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(userResetPassDto.getLogin());
    // Проверка на валидность старого пароля
    if (passwordEncoder.matches(userResetPassDto.getOldPass(), userDetails.getPassword())) {
      userResetPassDto.setNewPass(passwordEncoder.encode(userResetPassDto.getNewPass()));
      if(passwordService.resetPassword(userResetPassDto, userResetPassDto.getLogin())) {
        return Map.of("token", jwtUtil.generateToken(userResetPassDto.getLogin()));
      }
    }
    return Map.of("messages", "something was wrong while resetting password");
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> errorHandler(FLCException e) {
    ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis(), e.getCode());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
