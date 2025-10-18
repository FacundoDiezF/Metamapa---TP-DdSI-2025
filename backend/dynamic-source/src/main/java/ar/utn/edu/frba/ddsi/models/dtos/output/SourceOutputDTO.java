package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import lombok.Data;

@Data
public class SourceOutputDTO {
  Long inClientId;
  Origin type;
}
