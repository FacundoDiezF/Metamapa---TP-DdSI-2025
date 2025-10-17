package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import java.time.LocalDateTime;
import lombok.Data;


@Data
public class EventOutputDTO {
  Long id;
  Long sourceClientId;
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
    dto.setSourceId(event.getSourceId());
    dto.setOrigin(event.getOrigin());
    dto.setTitle(event.getTitle());
    dto.setDescription(event.getDescription());
    dto.setCategory(event.getCategory().getName()); //TODO: Verificar si esta bien pasar nombre o tenemos que pasar Id o un DTO
    dto.setLatitude(event.getLatitude());
    dto.setLongitude(event.getLongitude());
    dto.setEventDate(event.getEventDate());
    dto.setUploadDate(event.getUploadDate());
    return dto;
  }
}
