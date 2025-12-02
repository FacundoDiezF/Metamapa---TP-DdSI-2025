package interfaz.grafica.ddsi.dtos.auth;

import lombok.Data;
import java.util.List;

@Data
public class UserSummaryDTO {
  private String publicId; // UUID
  private String username;
//  private String email;
//  private String firstName;
//  private String lastName;
//  private String avatarUrl;
  private List<String> roles; // Ej: ["CONTRIBUYENTE", "ADMIN"]
}
