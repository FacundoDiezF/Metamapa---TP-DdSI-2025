package ar.utn.edu.frba.ddsi.models.factories;

import ar.utn.edu.frba.ddsi.models.dtos.RegisterRequestDTO;
import ar.utn.edu.frba.ddsi.models.entities.Role;
import ar.utn.edu.frba.ddsi.models.entities.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
@Component
public class UserFactory {
  private final PasswordEncoder passwordEncoder;

  public UserFactory(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public User createFrom(RegisterRequestDTO dto) {
    User user = User.from(dto);
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRoles(Set.of(Role.CONTRIBUTOR));
    user.setActive(true);
    return user;
  }
}
