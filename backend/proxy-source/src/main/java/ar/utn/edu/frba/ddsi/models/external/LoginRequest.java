package ar.utn.edu.frba.ddsi.models.external;

public class LoginRequest {
  public String email;
  public String password;

  public LoginRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
