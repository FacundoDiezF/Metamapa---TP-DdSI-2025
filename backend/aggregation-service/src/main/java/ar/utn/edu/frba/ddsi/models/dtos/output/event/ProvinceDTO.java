package ar.utn.edu.frba.ddsi.models.dtos.output.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Province;
import lombok.Data;

@Data
public class ProvinceDTO {
  private Long id;
  private String name;

  public ProvinceDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static ProvinceDTO from(Province province) {
    return new ProvinceDTO(province.getId(), province.getName());
  }

}
