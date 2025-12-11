package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.AuthResponseDTO;
import ar.utn.edu.frba.ddsi.models.dtos.LoginRequestDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RefreshTokenDTO;
import ar.utn.edu.frba.ddsi.models.dtos.RegisterRequestDTO;

public interface IAuthService {
  AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);
}
