package ar.utn.edu.frba.ddsi.filters;

import ar.utn.edu.frba.ddsi.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

  private final JwtUtils jwtUtils;

  public AuthenticationFilter(JwtUtils jwtUtils) {
    super(Config.class);
    this.jwtUtils = jwtUtils;
  }

  @Override
  public GatewayFilter apply(Config config) {
    return (exchange, chain) -> {
      ServerHttpRequest request = exchange.getRequest();
      boolean authenticationRequired = config.isRequired();

      System.out.println("Ruta: " + exchange.getRequest().getMethod()+ " " + exchange.getRequest().getPath() + " | Auth Required: " + authenticationRequired);

      // EXTRACCIÓN DEL TOKEN
      String token = extractToken(request);

      if (token == null) {
        if (!authenticationRequired) {
          return chain.filter(exchange); //Deja pasar la request como vino
        }
        return onError(exchange, HttpStatus.UNAUTHORIZED);
      }

      // VALIDACIÓN DE TOKEN
      if (jwtUtils.isInvalid(token) || !jwtUtils.isAccessToken(token)) { // Si es inválido o no es un access token
        return onError(exchange, HttpStatus.UNAUTHORIZED);
      }

      // VALIDACIÓN DE ROLES

      if (config.getRoles() != null && !config.getRoles().isEmpty()) { // Si la ruta tiene roles configurados, verificamos que el usuario tenga al menos uno.
        try {
          Claims claims = jwtUtils.getAllClaims(token);
          List<String> userRoles = claims.get("roles", List.class); // Asumimos que los roles vienen como una lista de Strings bajo la key "roles"

          boolean hasRole = userRoles.stream()
              .anyMatch(role -> config.getRoles().contains(role)); // Verificamos si el usuario tiene al menos uno de los roles requeridos

          if (!hasRole) {
            return onError(exchange, HttpStatus.FORBIDDEN); // 403: Token válido, pero sin permiso
          }

        } catch (Exception e) {
          return onError(exchange, HttpStatus.UNAUTHORIZED);
        }
      }

      // INYECCIÓN DE HEADERS PARA EL MICRO-SERVICIO
      // Pasamos la info del usuario ya validada para que el micro-servicio no tenga que parsear el token.
      try {
        Claims claims = jwtUtils.getAllClaims(token);
        ServerHttpRequest modifiedRequest = request.mutate()
            .header("X-User-Id", claims.get("userId", String.class))
            .header("X-User-Name", claims.getSubject())
            .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());

      } catch (Exception e) {
        return onError(exchange, HttpStatus.UNAUTHORIZED);
      }
    };
  }

  private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(httpStatus);
    return response.setComplete();
  }

  private String extractToken(ServerHttpRequest request) {
    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) return null; // Si no hay header Authorization

    List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
    if (authHeaders == null || authHeaders.isEmpty()) return null; // Si el header Authorization está vacío

    String authHeader = authHeaders.get(0);
    if (!authHeader.startsWith("Bearer ")) return null; // Si no es Bearer

    return authHeader.substring(7); // Retorna solo el token, sin "Bearer "
  }

  @Data
  public static class Config {
    private boolean required = true; // Por defecto es OBLIGATORIO
    private List<String> roles;
  }

  /**
   * Permite la sintaxis corta en YAML: AuthenticationFilter=ADMIN
   * Mapea los valores después del "=" al campo "roles" de la Config.
   */
  @Override
  public List<String> shortcutFieldOrder() {
    return List.of("roles"); //TODO Dice list pero solo sirve para tomar uno. Revisar esto
  }
}