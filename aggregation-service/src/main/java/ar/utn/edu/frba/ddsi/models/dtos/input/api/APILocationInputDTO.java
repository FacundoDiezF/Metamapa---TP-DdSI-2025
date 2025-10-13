package ar.utn.edu.frba.ddsi.models.dtos.input.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class APILocationInputDTO {
  @JsonProperty("lat")
  Double lat;
  @JsonProperty("lon")
  Double lon;
  @JsonProperty("provincia_nombre")
  String province;
  @JsonProperty("municipio_nombre")
  String municipality;
  @JsonProperty("departamento_nombre")
  String department;

  public static APILocationInputDTO from(LocationAPIResultInputDTO dto){
    return dto.getLocation();
  }
}
