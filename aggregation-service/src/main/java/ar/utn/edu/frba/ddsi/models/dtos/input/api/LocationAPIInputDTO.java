package ar.utn.edu.frba.ddsi.models.dtos.input.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LocationAPIInputDTO {
  @JsonProperty("resultados")
  List<LocationAPIResultInputDTO> results;
}
