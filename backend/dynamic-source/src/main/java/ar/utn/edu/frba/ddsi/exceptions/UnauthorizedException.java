package ar.utn.edu.frba.ddsi.exceptions;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) {
    super(message);
  }
}
