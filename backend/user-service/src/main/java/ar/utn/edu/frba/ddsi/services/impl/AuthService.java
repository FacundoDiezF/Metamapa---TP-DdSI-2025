package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.UsernameAlreadyExistsException;
import ar.utn.edu.frba.ddsi.models.dtos.AuthResponseDTO;
import ar.utn.edu.frba.ddsi.models.dtos.LoginRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RefreshTokenDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RegisterRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.UserSummaryDTO;
import ar.utn.edu.frba.ddsi.models.entities.Role;
import ar.utn.edu.frba.ddsi.models.entities.User;
import ar.utn.edu.frba.ddsi.models.repositories.IUserRepository;
import ar.utn.edu.frba.ddsi.services.IAuthService;
import ar.utn.edu.frba.ddsi.services.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService implements IAuthService {
  private final IUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Override
  public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
      throw new UsernameAlreadyExistsException();
    }

    User user = User.from(registerRequestDTO);

    user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

    user.setRoles(Set.of(Role.CONTRIBUTOR)); // Asignar rol por defecto

    userRepository.save(user);

    AuthResponseDTO authResponse = jwtService.generateTokens(user);
    authResponse.setUser(UserSummaryDTO.from(user));

    return authResponse;
  }
}
