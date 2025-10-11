package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class LocationDTO {
  Double latitude;
  Double longitude;

  String province;
  //TODO: Agregar los demas campos necesarios

  public LocationDTO(Double latitude, Double longitude, String province) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.province = province;
  }
}
