package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class LocationDTO {
  Double latitude;
  Double longitude;

  String province;
  String department;
  String municipality;

  public LocationDTO(Double latitude, Double longitude, String province, String department, String municipality) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.province = province;
    this.department = department;
    this.municipality = municipality;
  }
}
