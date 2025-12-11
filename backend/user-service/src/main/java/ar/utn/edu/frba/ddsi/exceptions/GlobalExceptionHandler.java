package ar.utn.edu.frba.ddsi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(UsernameAlreadyExistsException ex) {
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); //TODO actualizar el status que se catchea en el login del front para que muestre el error de usuario ya existente
  }
}
