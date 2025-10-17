package ar.utn.edu.frba.ddsi.exceptions;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(String errorCode, String message) {
      this.errorCode = errorCode;
      this.message = message;
    }
}
