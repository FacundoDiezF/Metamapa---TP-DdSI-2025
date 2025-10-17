package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import lombok.Data;

@Data
public class SourceOutputDTO {
  //Need it for the comunication with the agregationService
  Long sourceClientId;
  Long inClientId;
  Origin type;
}
