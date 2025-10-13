package ar.utn.edu.frba.ddsi.models.dtos.output;

import lombok.Data;

@Data
public class TopOutputDTO {

  String entityName;

  Long occurrences;

  public TopOutputDTO(String entityName, Long occurrences) {
    this.entityName = entityName;
    this.occurrences = occurrences;
  }
}
