package interfaz.grafica.ddsi.dtos;

import java.time.LocalDate;
import java.util.List;

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
  List<MultimediaDTO> resources;

  // Direct from a source use case
  Double latitude;
  Double longitude;
}
