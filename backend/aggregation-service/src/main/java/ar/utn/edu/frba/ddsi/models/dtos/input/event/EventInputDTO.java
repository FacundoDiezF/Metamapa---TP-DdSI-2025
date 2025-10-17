package ar.utn.edu.frba.ddsi.models.dtos.input.event;

import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventInputDTO {
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
}
