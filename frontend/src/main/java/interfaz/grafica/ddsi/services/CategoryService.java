package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import java.util.List;

import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CategoryService {

  private final ServerAdapter server;

  public CategoryService(ServerAdapter server){
    this.server = server;
  }

  public PageDTO<CategoryDTO> getAll(int page, int size) {
    try {
      return server.getAllCategories(page, size);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }
}
