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

  public TopOutputDTO(Integer hour, Long occurrences) {
    this.entityName = this.getFormattedHour(hour);
    this.occurrences = occurrences;
  }

  private String getFormattedHour(int hour) {
    int displayHour = (hour == 0 || hour == 12) ? 12 : hour % 12;
    String period = (hour < 12) ? "AM" : "PM";
    return displayHour + period;
  }
}
