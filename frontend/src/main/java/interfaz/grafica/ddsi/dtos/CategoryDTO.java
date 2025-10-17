package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class CategoryDTO {
  Long id;
  String name;

  public CategoryDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
