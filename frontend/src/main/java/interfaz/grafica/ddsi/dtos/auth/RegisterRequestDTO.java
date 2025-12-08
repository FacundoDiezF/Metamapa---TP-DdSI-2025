package interfaz.grafica.ddsi.dtos.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
  private String username;
  private String password;
//  private String email;
//  private String firstName;
//  private String lastName;
}