package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.auth.AuthResponseDTO;
import interfaz.grafica.ddsi.dtos.auth.RegisterRequestDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientAuthService {

  private final ServerAdapter serverAdapter;

  public ClientAuthService(ServerAdapter serverAdapter) {
    this.serverAdapter = serverAdapter;
  }

  public void registerAndAutoLogin(RegisterRequestDTO request, HttpSession session) {
    // 1. Llamar al Backend
    AuthResponseDTO response = serverAdapter.register(request);

    // 2. Guardar en Sesión
    session.setAttribute("accessToken", response.getAccessToken());
    session.setAttribute("refreshToken", response.getRefreshToken());
    session.setAttribute("user", response.getUser());

    // 3. Auto-Login en Spring Security
    List<SimpleGrantedAuthority> authorities = Collections.emptyList();
    if (response.getUser().getRoles() != null) {
      authorities = response.getUser().getRoles().stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList());
    }

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
        response.getUser().getUsername(),
        null,
        authorities
    );

    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}