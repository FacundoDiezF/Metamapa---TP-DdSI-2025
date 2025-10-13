package ar.utn.edu.frba.ddsi.models.dtos.output.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import lombok.Data;

@Data
public class LocationOutputDTO {
  Double latitude;
  Double longitude;

  String province;
  //TODO: Agregar los demas campos necesarios

  public static LocationOutputDTO from(Location location) {
    LocationOutputDTO dto = new LocationOutputDTO();
    dto.setLatitude(location.getLatitude());
    dto.setLongitude(location.getLongitude());
    dto.setProvince("Bs As");
    return dto;
  }
}
