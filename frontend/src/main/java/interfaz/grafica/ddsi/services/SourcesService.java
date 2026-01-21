package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SourcesService {

  private final ServerAdapter server;

  public SourcesService(ServerAdapter server) {
    this.server = server;
  }

  public List<SourceDTO> getAll() {
    return server.getAllSources(); 
  }

}
