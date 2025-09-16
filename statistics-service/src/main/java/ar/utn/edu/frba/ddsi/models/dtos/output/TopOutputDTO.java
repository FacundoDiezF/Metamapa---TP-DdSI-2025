package ar.utn.edu.frba.ddsi.models.dtos.output;

import lombok.Data;

@Data
public class TopOutputDTO {

  Long entityId;

  String entityName;

  Long occurrences;

  public TopOutputDTO(Long entityId, String entityName, Long occurrences) {
    this.entityId = entityId;
    this.entityName = entityName;
    this.occurrences = occurrences;
  }
}
