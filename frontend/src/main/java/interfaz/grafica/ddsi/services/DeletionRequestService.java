package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestEvaluateDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class DeletionRequestService {

  private final ServerAdapter server;

  public DeletionRequestService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<DeletionRequestDTO> getAll(int page, int size) {
    return server.getAllDeletionRequests(page, size);
  }

  public void postNew(DeletionRequestCreationDTO newDeletionRequest) {
    server.postNewDeletionRequest(newDeletionRequest); 
  }

  public void evaluate(Long eventId, DeletionRequestEvaluateDTO dto) {
    server.evaluateDeletionRequest(eventId, dto); 
  }

}
