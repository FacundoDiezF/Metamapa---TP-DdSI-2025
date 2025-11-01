package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class StatisticsDTO {

  String entityName;

  Long occurrences;

  public StatisticsDTO(String entityName, Long occurrences) {
    this.entityName = entityName;
    this.occurrences = occurrences;
  }
}
