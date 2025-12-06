package interfaz.grafica.ddsi.dtos.output;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventOutputDTO {
  String contributor = "borrar"; //TODO: Eliminar esto tanto de aca como del back cuando se implementen tokens (debe recuperar el usuario del token y guardar el id no el nombre).
  String title;
  String description;
  String category;
  Double latitude;
  Double longitude;
  LocalDate eventDate;
}
