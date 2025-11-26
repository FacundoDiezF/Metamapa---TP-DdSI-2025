package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;

@Service
public class DeletionRequestAdminService {

  private final ServerAdapter server;

  DeletionRequestAdminService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<DeletionRequestDTO> getAll(int page, int size) {
    return server.getAllDeletionRequests(page, size);
  }
}

