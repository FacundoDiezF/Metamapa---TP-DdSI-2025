package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.InactiveUserException;
import ar.utn.edu.frba.ddsi.exceptions.InvalidOrExpiredTokenException;
import ar.utn.edu.frba.ddsi.exceptions.NonExistentUserException;
import ar.utn.edu.frba.ddsi.exceptions.NonMatchingPasswordException;
import ar.utn.edu.frba.ddsi.exceptions.UsernameAlreadyExistsException;
import ar.utn.edu.frba.ddsi.models.dtos.AuthResponseDTO;
import ar.utn.edu.frba.ddsi.models.dtos.LoginRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RefreshTokenDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RegisterRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.UserSummaryDTO;
import ar.utn.edu.frba.ddsi.models.entities.User;
import ar.utn.edu.frba.ddsi.models.factories.UserFactory;
import ar.utn.edu.frba.ddsi.models.repositories.IUserRepository;
import ar.utn.edu.frba.ddsi.services.IAuthService;
import ar.utn.edu.frba.ddsi.services.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
  private final IUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserFactory userFactory;

  public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, UserFactory userFactory) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.userFactory = userFactory;
  }

  @Override
  public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
    if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
      throw new UsernameAlreadyExistsException();
    }

    User user = userFactory.createFrom(registerRequestDTO);

    userRepository.save(user);

    return authResponse(user);
  }

  @Override
  public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
    User user = userRepository.findByUsername(loginRequestDTO.getUsername())
        .orElseThrow(NonExistentUserException::new);

    if (!user.isActive()){
      throw new InactiveUserException();
    }

    if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
      throw new NonMatchingPasswordException();
    }

    return authResponse(user);
  }

  private AuthResponseDTO authResponse(User user) {
    AuthResponseDTO authResponse = jwtService.generateTokens(user);
    authResponse.setUser(UserSummaryDTO.from(user));
    return authResponse;
  }

  @Override
  public AuthResponseDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
    String refreshToken = refreshTokenDTO.getRefreshToken();

    String username;

    try {
      username = jwtService.extractUsername(refreshToken);
    } catch (Exception e) {
      throw new InvalidOrExpiredTokenException();
    }

    User user = userRepository.findByUsername(username) //? Debería obtenerlo por nombre de usuario o por id publica?
        .orElseThrow(NonExistentUserException::new);

    if (!user.isActive()){
      throw new InactiveUserException();
    }

    if(!jwtService.isRefreshTokenValid(refreshTokenDTO.getRefreshToken())) {
      throw new InvalidOrExpiredTokenException();
    }

    return authResponse(user);
  }
}
