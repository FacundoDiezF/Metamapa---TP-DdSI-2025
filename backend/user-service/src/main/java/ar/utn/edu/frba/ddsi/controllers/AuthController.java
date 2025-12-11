package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.AuthResponseDTO;
import ar.utn.edu.frba.ddsi.models.dtos.LoginRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RefreshTokenDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RegisterRequestDTO;
import ar.utn.edu.frba.ddsi.services.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final IAuthService authService;

  public AuthController(IAuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {

    return ResponseEntity.ok(authService.register(registerRequestDTO));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
    return ResponseEntity.ok(authService.login(loginRequestDTO));
  }

  @PostMapping("/token/refresh")
  public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO) { //? o devuelvo solo el token?
    return ResponseEntity.ok(authService.refreshToken(refreshTokenDTO));
  }
}
