package ar.utn.edu.frba.ddsi.models.dtos.input;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class EventDTO {
  String contributor; //TODO: Esto deberia ser un usuario

  String title;
  String description;
  String category;
  Double latitude;
  Double longitude;
  LocalDateTime eventDate;
}
