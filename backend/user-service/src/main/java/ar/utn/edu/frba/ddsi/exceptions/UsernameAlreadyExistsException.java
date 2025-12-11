package ar.utn.edu.frba.ddsi.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
  public UsernameAlreadyExistsException() {super("Username already exists");}
}
