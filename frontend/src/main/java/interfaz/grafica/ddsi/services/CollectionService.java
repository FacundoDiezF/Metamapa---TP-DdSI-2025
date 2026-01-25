package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.collection.CollectionOutputDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CollectionService {

  private final ServerAdapter server;

  public CollectionService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<CollectionDTO> getAll(int page , int size) {
    return server.getAllCollections(page , size); 
  }

  public CollectionDTO getByHandler(String handler) {
    return server.getCollection(handler); 
  }

  public void postNew(CollectionOutputDTO newCollection) {
    server.postNewCollection(newCollection); 
  }

  public void updateByHandler(String handler, CollectionOutputDTO updatedCollection) {
    server.updateCollection(handler, updatedCollection);
  }

  public void deleteByHandler(String handler) {
    server.deleteCollection(handler); 
  }

}
