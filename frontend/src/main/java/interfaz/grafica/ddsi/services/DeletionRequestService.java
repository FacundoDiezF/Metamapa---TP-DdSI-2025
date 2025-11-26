package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
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

  public void postNew(DeletionRequestCreationDTO newDeletionRequest) {
    try {
      server.postNewDeletionRequest(newDeletionRequest);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

}
