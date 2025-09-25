package ar.utn.edu.frba.ddsi.models.external;

import lombok.Data;

@Data
public class LoginResponse {
  LoginData data;

  public String getAccessToken() {
    return data.getAccess_token();
  }
}
