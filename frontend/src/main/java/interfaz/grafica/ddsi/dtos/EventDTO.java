package interfaz.grafica.ddsi.dtos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class EventDTO {
  Long id;
  Long sourceId;
  Origin origin;
  String title;
  String description;
  String category;
  LocationDTO location;
  LocalDate eventDate;

  public EventDTO(Long id, Long sourceId, Origin origin, String title, String description,
                  String category, LocationDTO location, LocalDate eventDate
  ) {
    this.id = id;
    this.sourceId = sourceId;
    this.origin = origin;
    this.title = title;
    this.description = description;
    this.category = category;
    this.location = location;
    this.eventDate = eventDate;
  }
}
