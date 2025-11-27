package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.SubmissionRequestDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;

@Service
public class SubmissionRequestService {

  private final ServerAdapter server;

  public SubmissionRequestService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<SubmissionRequestDTO> getAll(int page, int size) {
    return server.getAllSubmissionRequest(page, size);
  }
}
