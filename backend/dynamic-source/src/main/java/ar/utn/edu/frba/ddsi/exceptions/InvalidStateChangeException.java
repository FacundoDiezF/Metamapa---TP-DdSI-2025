package ar.utn.edu.frba.ddsi.exceptions;

public class InvalidStateChangeException extends RuntimeException {
  public InvalidStateChangeException(String message) {
    super(message);
  }
}
