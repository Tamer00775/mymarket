package kz.kartayev.authorization_service.security;

import kz.kartayev.authorization_service.commons.adapter.UserDtoAdapter;
import kz.kartayev.authorization_service.commons.dto.RoleDto;
import kz.kartayev.authorization_service.commons.dto.UserDto;
import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserRole;
import kz.kartayev.authorization_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class PersonDetails implements UserDetails {
  private final User user;
  private final List<UserRole> userRoles;

  @Override
  @Transactional(readOnly = true)
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    userRoles.forEach(role -> {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getUserRoles().toString()));
    });
    return grantedAuthorities;
  }

  @Override
  public String getPassword() {
    return user.getUserCredentials().getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.getIsActive();
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {

    return user.getUserCredentials().getPassExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.getIsActive();
  }

  public User getUser() {
    return this.user;
  }

}
