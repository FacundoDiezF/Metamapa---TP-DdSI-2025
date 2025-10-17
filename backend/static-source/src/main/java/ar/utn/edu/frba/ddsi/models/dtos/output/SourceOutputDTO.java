package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import lombok.Data;

@Data
public class SourceOutputDTO {
  //Need it for the comunication with the agregationService
  Long sourceClientId;

  Long inClientId;
  Origin type;

  String path;

  public static SourceOutputDTO from(Source source) {

    SourceOutputDTO dto = new SourceOutputDTO();
    dto.setInClientId(source.getId());
    dto.setType(source.getType());
    dto.setPath(source.getPath());

    return dto;
  }


}
