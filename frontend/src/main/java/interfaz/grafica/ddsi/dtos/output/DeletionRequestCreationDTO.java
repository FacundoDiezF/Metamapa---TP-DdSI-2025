package interfaz.grafica.ddsi.dtos.output;

import lombok.Data;

@Data
public class DeletionRequestCreationDTO {
  Long eventId;
  String argument;
  String applicantName = "nombre"; //TODO: VER QUE SE LE DEBERIA PASAR ACA, ACTUALMENTE HARDCODEADO
}
