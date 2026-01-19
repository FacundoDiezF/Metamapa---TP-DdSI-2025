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
    try {
      return server.getAllCollections(page , size);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public CollectionDTO getByHandler(String handler) {
    try {
      return server.getCollection(handler);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public void postNew(CollectionOutputDTO newCollection) {
    try {
      server.postNewCollection(newCollection);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public void updateByHandler(String handler, CollectionOutputDTO updatedCollection) {
    try {
      server.updateCollection(handler, updatedCollection);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    }
    //* quiero sacar esta excepcion porque opaca el manejo de excepciones que hay por atras en el back y termina saliendo siempre el mismo mensaje
//    catch (Exception e) {
//      throw new ConnectionException("Could not connect to backend");
//    }
  }

  public void deleteByHandler(String handler) {
    try {
      server.deleteCollection(handler);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

}
