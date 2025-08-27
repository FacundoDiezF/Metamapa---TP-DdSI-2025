package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
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
  LocalDateTime updateDate;
  LocalDateTime uploadDate;

  public static EventOutputDTO from(EventInputDTO dto) {
    EventOutputDTO eventOutputDTO = new EventOutputDTO();
    eventOutputDTO.setId(dto.getId());
    eventOutputDTO.setSourceId(dto.getSourceId());
    eventOutputDTO.setOrigin(dto.getOrigin());
    eventOutputDTO.setTitle(dto.getTitle());
    eventOutputDTO.setDescription(dto.getDescription());
    eventOutputDTO.setCategory(dto.getCategory());
    eventOutputDTO.setLatitude(dto.getLatitude());
    eventOutputDTO.setLongitude(dto.getLongitude());
    eventOutputDTO.setEventDate(dto.getEventDate());
    eventOutputDTO.setUpdateDate(dto.getUpdateDate());
    eventOutputDTO.setUploadDate(dto.getUpdateDate()); //! Cuando exista, va a recibir uploadDate
    return eventOutputDTO;
  }

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
    dto.setUpdateDate(event.getUpdateDate());
    dto.setUploadDate(event.getUploadDate());
    return dto;
  }

}
