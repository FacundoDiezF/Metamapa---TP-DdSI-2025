package ar.utn.edu.frba.ddsi.exceptions;

public class InvalidOrExpiredTokenException extends RuntimeException {
  public InvalidOrExpiredTokenException() {
    super("Invalid or expired token");
  }
}
