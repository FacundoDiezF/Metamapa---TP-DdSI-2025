package interfaz.grafica.ddsi.dtos;

import java.time.LocalDate;
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

  // Direct from a source use case
  Double latitude;
  Double longitude;
}
