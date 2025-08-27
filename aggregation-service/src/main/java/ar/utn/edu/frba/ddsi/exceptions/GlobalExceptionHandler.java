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

  @ExceptionHandler(SpamException.class)
  public ResponseEntity<ErrorResponse> handleSpamDetectedException(SpamException ex) {
    ErrorResponse errorResponse = new ErrorResponse("SPAM_DETECTED", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(SubscriptionException.class)
  public ResponseEntity<ErrorResponse> handleSubscriptionException(RuntimeException ex) {
    ErrorResponse errorResponse = new ErrorResponse("SUBSCRIPTION_ERROR", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
  }
}
