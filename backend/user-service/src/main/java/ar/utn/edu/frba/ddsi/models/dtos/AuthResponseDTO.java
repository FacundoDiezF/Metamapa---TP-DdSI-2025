package ar.utn.edu.frba.ddsi.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {
  private String accessToken;
  private String refreshToken;
  private UserSummaryDTO user;
}