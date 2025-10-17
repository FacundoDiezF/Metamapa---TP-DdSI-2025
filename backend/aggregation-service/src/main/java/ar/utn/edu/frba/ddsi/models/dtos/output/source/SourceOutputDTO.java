package ar.utn.edu.frba.ddsi.models.dtos.output.source;

import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import lombok.Data;

@Data
public class SourceOutputDTO {

  Long id;
  Long clientId;
  Origin type;

  public static SourceOutputDTO from(Source source) {
    SourceOutputDTO dto = new SourceOutputDTO();
    dto.id = source.getId();
    dto.clientId = source.getSourceClient().getId();
    dto.type = source.getType();
    return dto;
  }
}
