package ar.utn.edu.frba.ddsi.exceptions;

public class NonExistentUserException extends RuntimeException {
  public NonExistentUserException() {super("Invalid username or password");} //Mensaje ambiguo para evitar filtrar información sobre los usuarios existentes
}
