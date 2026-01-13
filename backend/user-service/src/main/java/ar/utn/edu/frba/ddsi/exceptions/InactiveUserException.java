package ar.utn.edu.frba.ddsi.exceptions;

public class InactiveUserException extends RuntimeException {
  public InactiveUserException() {
    super("User account is inactive");
  }
}
