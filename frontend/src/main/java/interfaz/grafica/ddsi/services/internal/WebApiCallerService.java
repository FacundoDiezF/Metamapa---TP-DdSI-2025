package interfaz.grafica.ddsi.services.internal;
import interfaz.grafica.ddsi.dtos.auth.AuthResponseDTO;
import interfaz.grafica.ddsi.dtos.auth.RefreshTokenDTO;
import interfaz.grafica.ddsi.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Servicio genérico para hacer llamadas HTTP con manejo automático de tokens
 */
@Service
public class WebApiCallerService {

  private final WebClient apiGatewayClient;
  private final String authServiceUrl;

  public WebApiCallerService(@Value("${api.gateway.url}") String apiGatewayUrl) {
    this.apiGatewayClient = WebClient.builder()
        .baseUrl(apiGatewayUrl)
        .build();
    this.authServiceUrl = apiGatewayUrl;
  }

  /**
   * Ejecuta una llamada al API con manejo automático de refresh token
   * @param apiCall función que ejecuta la llamada al API
   * @return resultado de la llamada al API
   */
  public <T> T executeWithTokenRetry(ApiCall<T> apiCall) {
    String accessToken = getAccessTokenFromSession();
    String refreshToken = getRefreshTokenFromSession();

    //! Por ahora voy a sacar esto porque no me parece necesario. Hay peticiones que se deberian poder ejecutar sin token, y es responsabilidad del api gateway validar el token (o su ausencia)
////    if (accessToken == null) {
////      throw new RuntimeException("No hay token de acceso disponible");
////    }

    try {
      // Primer intento con el token actual
      return apiCall.execute(this.authHeaderValue(accessToken));
    } catch (WebClientResponseException e) {
      if ((e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) && refreshToken != null) {
        try {
          // Token expirado, intentar refresh
          AuthResponseDTO newTokens = refreshToken(refreshToken);

          // Segundo intento con el nuevo token
          return apiCall.execute(newTokens.getAccessToken());
        } catch (Exception refreshError) {
          throw new RuntimeException("Error al refrescar token y reintentar: " + refreshError.getMessage(), refreshError);
        }
      }
      if(e.getStatusCode() == HttpStatus.NOT_FOUND){
        throw new NotFoundException(e.getMessage());
      }
      throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP GET
   */
  public <T> T get(String url, Class<T> responseType) {
    return executeWithTokenRetry(authHeaderValue ->
        apiGatewayClient
            .get()
            .uri(url)
            .header("Authorization", authHeaderValue)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP GET que retorna una lista
   */
  public <T> java.util.List<T> getList(String url, Class<T> responseType) {
    return executeWithTokenRetry(authHeaderValue ->
        apiGatewayClient
            .get()
            .uri(url)
            .header("Authorization", authHeaderValue)
            .retrieve()
            .bodyToFlux(responseType)
            .collectList()
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP GET con un token específico (sin usar sesión)
   */
  public <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
    try {
      return apiGatewayClient
          .get()
          .uri(url)
          .header("Authorization", this.authHeaderValue(accessToken))
          .retrieve()
          .bodyToMono(responseType)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP POST
   */
  public <T> T post(String url, Object body, Class<T> responseType) {
    return executeWithTokenRetry(authHeaderValue ->
        apiGatewayClient
            .post()
            .uri(url)
            .header("Authorization", authHeaderValue)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP PUT
   */
  public <T> T put(String url, Object body, Class<T> responseType) {
    return executeWithTokenRetry(authHeaderValue ->
        apiGatewayClient
            .put()
            .uri(url)
            .header("Authorization", authHeaderValue)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP DELETE
   */
  public void delete(String url) {
    executeWithTokenRetry(authHeaderValue -> {
      apiGatewayClient
          .delete()
          .uri(url)
          .header("Authorization", authHeaderValue)
          .retrieve()
          .bodyToMono(Void.class)
          .block();
      return null;
    });
  }

  /**
   * Refresca el access token usando el refresh token
   */
  private AuthResponseDTO refreshToken(String refreshToken) {
    try {
      RefreshTokenDTO refreshRequest = RefreshTokenDTO.builder()
          .refreshToken(refreshToken)
          .build();

      AuthResponseDTO response = apiGatewayClient
          .post()
          .uri(authServiceUrl + "/auth/refresh")
          .bodyValue(refreshRequest)
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)
          .block();

      // Actualizar tokens en sesión
      updateTokensInSession(response.getAccessToken(), response.getRefreshToken());
      return response;
    } catch (Exception e) {
      throw new RuntimeException("Error al refrescar token: " + e.getMessage(), e);
    }
  }

  /**
   * Obtiene el access token de la sesión
   */
  private String getAccessTokenFromSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    return (String) request.getSession().getAttribute("accessToken");
  }

  /**
   * Obtiene el refresh token de la sesión
   */
  private String getRefreshTokenFromSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    return (String) request.getSession().getAttribute("refreshToken");
  }

  /**
   * Actualiza los tokens en la sesión
   */
  private void updateTokensInSession(String accessToken, String refreshToken) {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    request.getSession().setAttribute("accessToken", accessToken);
    request.getSession().setAttribute("refreshToken", refreshToken);
  }

  /**
   * Interfaz funcional para ejecutar llamadas al API con token
   */
  @FunctionalInterface
  public interface ApiCall<T> {
    T execute(String accessToken) throws Exception;
  }

  /**
   * Agrega el header Authorization teniendo en cuenta si es un usuario logueado o no
   */
  private String authHeaderValue(String accessToken) {
    if (accessToken != null) {
      return "Bearer " + accessToken;
    } else {
      return null;
    }
  }
}

