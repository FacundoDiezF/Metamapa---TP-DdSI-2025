package interfaz.grafica.ddsi.dtos;

import java.time.LocalDateTime;
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
  LocalDateTime eventDate;
  LocalDateTime uploadDate;
  LocalDateTime updatedDate;

  public EventDTO(Long id, Long sourceId, Origin origin, String title, String description,
                  String category, LocationDTO location, LocalDateTime eventDate, LocalDateTime uploadDate,
                  LocalDateTime updatedDate) {
    this.id = id;
    this.sourceId = sourceId;
    this.origin = origin;
    this.title = title;
    this.description = description;
    this.category = category;
    this.location = location;
    this.eventDate = eventDate;
    this.uploadDate = uploadDate;
    this.updatedDate = updatedDate;
  }
}
