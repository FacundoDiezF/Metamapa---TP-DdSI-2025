package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  public List<CategoryDTO> getAllCategories() {
    //TODO: Que le pida al agregador las categorias
    // GET/categories
    return mockCategories();
  }

  //! BORRAR CUANDO ESTE EL AGREGADOR
  private List<CategoryDTO> mockCategories() {
    return List.of(
        new CategoryDTO(1L, "Accidente"),
        new CategoryDTO(2L, "Desastre Natural"),
        new CategoryDTO(3L, "Conflicto Social"),
        new CategoryDTO(4L, "Evento Político")
    );
  }
}
