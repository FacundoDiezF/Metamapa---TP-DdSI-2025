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
  LocationOutputDTO location;
  LocalDateTime eventDate;
  LocalDateTime uploadDate;
  LocalDateTime updatedDate;

  public static EventOutputDTO from(Event event) {
    EventOutputDTO dto = new EventOutputDTO();
    dto.setId(event.getId());
    dto.setSourceId(event.getSource().getId());
    dto.setTitle(event.getTitle());
    dto.setDescription(event.getDescription());
    dto.setCategory(event.getCategory().getName());
    dto.setOrigin(event.getSource().getType());
    dto.setLocation(LocationOutputDTO.from(event.getLocation()));
    dto.setEventDate(event.getEventDate());
    dto.setUploadDate(event.getUploadDate());
    dto.setUpdatedDate(event.getUpdatedDate());
    return dto;
  }
}
