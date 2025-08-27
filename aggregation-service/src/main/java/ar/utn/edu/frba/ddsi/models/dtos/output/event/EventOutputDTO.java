package ar.utn.edu.frba.ddsi.models.dtos.output.event;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventOutputDTO {
  Long id;
  Long sourceId;
  Origin origin;
  String title;
  String description;
  String category;
  Double latitude;
  Double longitude;
  LocalDateTime eventDate;
  LocalDateTime uploadDate;

  public static EventOutputDTO from(Event event) {
    EventOutputDTO dto = new EventOutputDTO();
    dto.setId(event.getId());
    dto.setSourceId(event.getSource().getId());
    dto.setTitle(event.getTitle());
    dto.setDescription(event.getDescription());
    dto.setCategory(event.getCategory().getName());
    dto.setOrigin(event.getSource().getType());
    dto.setLatitude(event.getLatitude());
    dto.setLongitude(event.getLongitude());
    dto.setEventDate(event.getEventDate());
    dto.setUploadDate(event.getUploadDate());
    return dto;
  }
}
