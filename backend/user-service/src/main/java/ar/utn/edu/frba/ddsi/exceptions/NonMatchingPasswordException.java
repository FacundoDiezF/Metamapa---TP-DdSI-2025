package ar.utn.edu.frba.ddsi.exceptions;

public class NonMatchingPasswordException extends RuntimeException {
  public NonMatchingPasswordException() {super("Invalid username or password");} //Mensaje ambiguo para evitar filtrar información sobre los usuarios existentes
}
