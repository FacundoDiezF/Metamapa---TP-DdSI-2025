package ar.utn.edu.frba.ddsi.models.dtos.output.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LocationAPIOutputDTO {
  @JsonProperty("ubicaciones")
  private List<LocationOutputDTO> locations;
}
