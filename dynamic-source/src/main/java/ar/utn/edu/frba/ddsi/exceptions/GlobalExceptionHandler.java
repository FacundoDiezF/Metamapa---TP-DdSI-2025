package ar.utn.edu.frba.ddsi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(NotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse("NOT_FOUND", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InvalidStateChangeException.class)
  public ResponseEntity<ErrorResponse> handleSpamDetectedException(RuntimeException ex) {
    ErrorResponse errorResponse = new ErrorResponse("INVALID_STATE_CHANGE", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleSubscriptionException(RuntimeException ex) {
    ErrorResponse errorResponse = new ErrorResponse("UNAUTHORIZED", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(RuntimeException ex) {
    ErrorResponse errorResponse = new ErrorResponse("ILLEGAL_STATE", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
