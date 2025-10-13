package ar.utn.edu.frba.ddsi.models.dtos.input;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventInputDTO {
  Long id;
  Long sourceId;
  String origin;
  String title;
  String description;
  String category;
  LocationInputDTO location;
  LocalDateTime eventDate;
  LocalDateTime uploadDate;
}
