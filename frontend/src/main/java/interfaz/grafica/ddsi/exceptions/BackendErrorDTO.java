package interfaz.grafica.ddsi.exceptions;

import lombok.Data;

@Data
public class BackendErrorDTO {
  private String errorCode;
  private String message;
}
