package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CategoryService {

  private final ServerAdapter server;

  public CategoryService(ServerAdapter server) {
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

  public List<CategoryDTO> getAllByIds(List<Long> categoriesIds) {
    try {
      return server.getAllCategoriesByIds(categoriesIds);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public CategoryDTO getById(Long categoryIds) {
    try {
      return server.getCategoryById(categoryIds);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }
}
