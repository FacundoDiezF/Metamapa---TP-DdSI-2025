package interfaz.grafica.ddsi.dtos.auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private UserSummaryDTO user;
}