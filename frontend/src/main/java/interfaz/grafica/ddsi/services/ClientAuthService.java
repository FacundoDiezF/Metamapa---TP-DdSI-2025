package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.auth.AuthResponseDTO;
import interfaz.grafica.ddsi.dtos.auth.RegisterRequestDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import interfaz.grafica.ddsi.providers.CustomAuthProvider;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ClientAuthService {

  private final ServerAdapter serverAdapter;
  private final CustomAuthProvider customAuthProvider;

  public ClientAuthService(ServerAdapter serverAdapter, CustomAuthProvider customAuthProvider) {
    this.serverAdapter = serverAdapter;
    this.customAuthProvider = customAuthProvider;
  }

  public void registerAndAutoLogin(RegisterRequestDTO request, HttpSession session) {
    // 1. Llamar al Backend
    AuthResponseDTO response = serverAdapter.register(request);

    // 2. Guardar en Sesión
    UsernamePasswordAuthenticationToken auth = customAuthProvider.createAuthenticationToken(session, response);

    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}