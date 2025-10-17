package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.category.CategoryDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {

  Category findOrCreate(String categoryName);

  Page<CategoryDTO> getAll(Pageable pageable);
}
