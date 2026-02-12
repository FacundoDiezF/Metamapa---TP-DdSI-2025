package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SourcesService {

  private final ServerAdapter server;

  public SourcesService(ServerAdapter server) {
    this.server = server;
  }

  public List<SourceDTO> getAll() {
    return server.getAllSources();
  }

  public void postNewSource(MultipartFile csvfile) {
    server.postNewSource(csvfile);
  }

}
