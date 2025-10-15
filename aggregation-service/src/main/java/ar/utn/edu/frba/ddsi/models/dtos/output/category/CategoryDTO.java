package ar.utn.edu.frba.ddsi.models.dtos.output.category;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import lombok.Data;

@Data
public class CategoryDTO {
  Long id;
  String name;

  public static CategoryDTO from(Category category) {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(category.getId());
    dto.setName(category.getName());
    return dto;
  }
}