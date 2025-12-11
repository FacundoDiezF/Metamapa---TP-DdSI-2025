package ar.utn.edu.frba.ddsi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ErrorResponse> handleException(Exception ex, HttpStatus statusCode) {
    ErrorResponse errorResponse = new ErrorResponse(statusCode.getReasonPhrase(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, statusCode);
  }

  @ExceptionHandler(UsernameAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
    return handleException(ex, HttpStatus.CONFLICT); //TODO actualizar el status que se catchea en el login del front para que muestre el error correspondiente
  }

  @ExceptionHandler(NonExistentUserException.class)
  public ResponseEntity<ErrorResponse> handleNonExistentUserException(NonExistentUserException ex) {
    return handleException(ex, HttpStatus.UNAUTHORIZED); //TODO actualizar el status que se catchea en el login del front para que muestre el error correspondiente
  }

  @ExceptionHandler(NonMatchingPasswordException.class)
  public ResponseEntity<ErrorResponse> handleNonMatchingPasswordException(NonMatchingPasswordException ex) {
    return handleException(ex, HttpStatus.UNAUTHORIZED); //TODO actualizar el status que se catchea en el login del front para que muestre el error correspondiente
  }
}
