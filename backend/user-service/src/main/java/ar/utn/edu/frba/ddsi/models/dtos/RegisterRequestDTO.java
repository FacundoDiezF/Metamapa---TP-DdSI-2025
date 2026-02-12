package ar.utn.edu.frba.ddsi.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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