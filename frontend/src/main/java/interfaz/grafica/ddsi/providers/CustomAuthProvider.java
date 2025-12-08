package interfaz.grafica.ddsi.providers;

import interfaz.grafica.ddsi.dtos.auth.AuthResponseDTO;
import interfaz.grafica.ddsi.dtos.auth.LoginRequestDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

  private final ServerAdapter serverAdapter;

  public CustomAuthProvider(@Lazy ServerAdapter serverAdapter) {
    this.serverAdapter = serverAdapter;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      // 1. Petición de login al servidor externo
      LoginRequestDTO requestDTO = new LoginRequestDTO(username, password);
      AuthResponseDTO response = serverAdapter.login(requestDTO);

      // 2. Guardar tokens en sesión
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      HttpServletRequest request = attributes.getRequest();
      HttpSession session = request.getSession();

      session.setAttribute("accessToken", response.getAccessToken());
      session.setAttribute("refreshToken", response.getRefreshToken());

      // 3. Guardar información relevante del usuario en sesión
      //? No estoy seguro si esta info es relevante pero por ahora la guardo, de ultima despues se saca
      session.setAttribute("publicId", response.getUser().getPublicId());
      session.setAttribute("username", response.getUser().getUsername());

      // 4. Guardar los permisos en sesión
      List<GrantedAuthority> authorities = new ArrayList<>();
      if (response.getUser().getRoles() != null) {
        authorities = response.getUser().getRoles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Ej: "ROLE_ADMIN"
            .collect(Collectors.toList());
      }

      // 5. Retornar el token de éxito (Importante: password null para seguridad)
      return new UsernamePasswordAuthenticationToken(
          username,
          null,
          authorities
      );
    } catch (ConnectionException e) {
      // TRADUCCIÓN DE EXCEPCIÓN:
      // Convertimos nuestro error de conexión en un error de Autenticación interno
      throw new AuthenticationServiceException("Server not available. Try later.", e);
    } catch (BackendException e) {
      // Si el backend devuelve otro error raro (500), también lo envolvemos
      throw new AuthenticationServiceException("Auth server error.", e);
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}