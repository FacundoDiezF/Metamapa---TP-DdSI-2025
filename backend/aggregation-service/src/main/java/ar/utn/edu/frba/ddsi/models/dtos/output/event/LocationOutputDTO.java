package ar.utn.edu.frba.ddsi.models.dtos.output.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import lombok.Data;

@Data
public class LocationOutputDTO {
  Double latitude;
  Double longitude;
  String province;
  String municipality;
  String department;

  public static LocationOutputDTO from(Location location) { //TODO TEST
    LocationOutputDTO dto = new LocationOutputDTO();
    dto.setLatitude(location.getCoordinate().getLatitude());
    dto.setLongitude(location.getCoordinate().getLongitude());
    dto.setProvince(location.getProvince() != null ? location.getProvince().getName() : null);
    dto.setMunicipality(location.getMunicipality() != null ? location.getMunicipality().getName() : null);
    dto.setDepartment(location.getDepartment() != null ? location.getDepartment().getName() : null);
    return dto;
  }
}
