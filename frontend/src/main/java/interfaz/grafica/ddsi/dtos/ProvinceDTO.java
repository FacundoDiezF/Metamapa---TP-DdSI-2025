package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class ProvinceDTO {
  Long id;
  String name;

  public ProvinceDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
