package interfaz.grafica.ddsi.dtos.output;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventOutputDTO {
  String title;
  String description;
  Long categoryId;
  Double latitude;
  Double longitude;
  LocalDate eventDate;
}
