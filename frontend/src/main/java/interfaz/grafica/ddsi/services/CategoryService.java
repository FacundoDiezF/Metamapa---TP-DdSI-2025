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

  public PageDTO<CategoryDTO> getAllCategories(int page, int size) {
    //TODO: Que le pida al agregador las categorias
    // GET/categories
    try {
      //return server.getAllCategories(page, size);
      return this.mockCategoryPage();
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  //! BORRAR CUANDO ESTE EL AGREGADOR
  private PageDTO<CategoryDTO> mockCategoryPage() {
    List<CategoryDTO> categories = List.of(
        new CategoryDTO(1L, "Accidente"),
        new CategoryDTO(2L, "Desastre Natural"),
        new CategoryDTO(3L, "Conflicto Social"),
        new CategoryDTO(4L, "Evento Político"),
        new CategoryDTO(5L, "Otro"),
        new CategoryDTO(6L, "Emergencia Médica"),
        new CategoryDTO(7L, "Incendio"),
        new CategoryDTO(8L, "Inundación"),
        new CategoryDTO(9L, "Terremoto"),
        new CategoryDTO(10L, "Huracán"),
        new CategoryDTO(11L, "Tornado"),
        new CategoryDTO(12L, "Erupción Volcánica"),
        new CategoryDTO(13L, "Deslizamiento de Tierra"),
        new CategoryDTO(14L, "Avalancha"),
        new CategoryDTO(15L, "Accidente"),
        new CategoryDTO(16L, "Desastre Natural"),
        new CategoryDTO(17L, "Conflicto Social"),
        new CategoryDTO(18L, "Evento Político"),
        new CategoryDTO(19L, "Otro"),
        new CategoryDTO(20L, "Emergencia Médica"),
        new CategoryDTO(21L, "Incendio"),
        new CategoryDTO(22L, "Inundación"),
        new CategoryDTO(23L, "Terremoto"),
        new CategoryDTO(24L, "Huracán"),
        new CategoryDTO(25L, "Tornado"),
        new CategoryDTO(26L, "Erupción Volcánica"),
        new CategoryDTO(27L, "Deslizamiento de Tierra"),
        new CategoryDTO(28L, "Avalancha")
    );
    return new PageDTO<>(
        categories,
        3,
        categories.size(),
        1,               // pageNumber
        false,            // first
        false             // last
    );
  }
}
