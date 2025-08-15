package ar.utn.edu.frba.ddsi.models.dtos.input.source;

import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import lombok.Data;

@Data
public class SourceInputDTO {
  Long sourceClientId;
  Long inClientId;
  Origin type;
}
