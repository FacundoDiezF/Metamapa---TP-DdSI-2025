package ar.utn.edu.frba.ddsi.models.dtos.input;

import lombok.Data;

@Data
public class LocationInputDTO {
  Double latitude;
  Double longitude;

  String province;
}
