package kz.kartayev.authorization_service.service;

import kz.kartayev.authorization_service.entity.User;
import kz.kartayev.authorization_service.entity.UserRole;
import kz.kartayev.authorization_service.repository.UserRepository;
import kz.kartayev.authorization_service.repository.UserRoleRepository;
import kz.kartayev.authorization_service.security.PersonDetails;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PersonDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  private final UserRoleRepository userRoleRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optUser = userRepository.findUserByUsername(username);
    if(optUser.isEmpty()) {
      throw new UsernameNotFoundException("username-not-found");
    }
    User user = optUser.get();
    List<UserRole> roles = userRoleRepository.findAllByUser(user);
    return new PersonDetails(user, roles);
  }
}
