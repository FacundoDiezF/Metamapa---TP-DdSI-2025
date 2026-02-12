package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonToken;
import lombok.Builder;
import lombok.Data;

@Builder
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
        return EventOutputDTO.builder()
                .id(event.getId())
                .sourceId(event.getSourceId())
                .origin(event.getOrigin())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory().getName()) // correcto usar el name mientras category sea entidad
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .eventDate(event.getEventDate())
                .uploadDate(event.getUploadDate())
                .build();
    }


}
