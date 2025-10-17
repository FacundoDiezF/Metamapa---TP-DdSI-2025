package ar.utn.edu.frba.ddsi.models.dtos.input.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LocationAPIResultInputDTO {
  @JsonProperty("ubicacion")
  APILocationInputDTO location;
}
