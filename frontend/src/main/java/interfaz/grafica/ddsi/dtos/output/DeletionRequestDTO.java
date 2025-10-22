package interfaz.grafica.ddsi.dtos.output;

import lombok.Data;

@Data
public class DeletionRequestDTO {
  Long eventId;
  String argument;
  String applicantName; //TODO: Esto deberia ser un usuario
}
