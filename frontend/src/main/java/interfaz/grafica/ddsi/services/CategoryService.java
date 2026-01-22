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
    return server.getAllCategories(page, size); 
  }

  public List<CategoryDTO> getAllByIds(List<Long> categoriesIds) {
    return server.getAllCategoriesByIds(categoriesIds); 
  }

  public CategoryDTO getById(Long categoryIds) {
    return server.getCategoryById(categoryIds); 
  }
}
