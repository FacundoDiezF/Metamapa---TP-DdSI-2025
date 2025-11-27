package interfaz.grafica.ddsi.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class BackendException extends RuntimeException {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Getter
  private BackendErrorDTO errorInfo;

  public BackendException(WebClientResponseException cause) {
    super("Backend error: " + cause.getStatusCode(), cause);

    try {
      String responseBody = cause.getResponseBodyAsString();
      this.errorInfo = mapper.readValue(responseBody, BackendErrorDTO.class);
    } catch (Exception e) {
      this.errorInfo = null;
    }
  }
}

