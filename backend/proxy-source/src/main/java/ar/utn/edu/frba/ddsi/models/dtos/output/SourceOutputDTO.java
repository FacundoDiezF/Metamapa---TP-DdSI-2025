package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import lombok.Data;

@Data
public class SourceOutputDTO {
  Long sourceClientId;
  Long inClientId;
  Origin type;

  public static SourceOutputDTO from(Source source) {
    SourceOutputDTO dto = new SourceOutputDTO();
    dto.setInClientId(source.getId());
    dto.setType(source.getType());
    return dto;
  }
}
