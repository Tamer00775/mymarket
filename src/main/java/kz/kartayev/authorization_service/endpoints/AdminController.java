package kz.kartayev.authorization_service.endpoints;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.service.AdminService;
import kz.kartayev.authorization_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin")
@ApiModel(description = "Управление админа")
public class AdminController {
  private final AdminService adminService;

  @GetMapping("/users")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @ApiOperation("Достает всех пользователей системы")
  public ResponseEntity<List<UserDto>> users() {
    return ResponseEntity.ok(adminService.findAll());
  }

  @PostMapping("/users/{id}/assign")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @ApiOperation("Назначение админов в систему")
  public ResponseEntity<HttpStatus> assignAdmin(@PathVariable("id") Long userId) {
    adminService.assignAdmin(userId);
    return ResponseEntity.ok(HttpStatus.OK);
  }

  @PostMapping("/users/{id}/block")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  @ApiOperation("Блокировка пользователя в системе")
  public ResponseEntity<HttpStatus> blockUser(@PathVariable("id") Long userId) {
    adminService.blockUser(userId);
    return ResponseEntity.ok(HttpStatus.OK);
  }
}
