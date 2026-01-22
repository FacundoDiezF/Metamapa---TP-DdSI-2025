package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.EditionRequestDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;

@Service
public class EditionRequestService {

  private final ServerAdapter server;

  public EditionRequestService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<EditionRequestDTO> getAll(int page, int size) {
    return server.getAllEditionRequests(page, size);
  }
}
