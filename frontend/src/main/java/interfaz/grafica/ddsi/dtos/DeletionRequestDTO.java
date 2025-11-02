package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class DeletionRequestDTO {
  EventDTO event;
  String argument;
  String applicantName = "nombre"; //TODO: VER QUE SE LE DEBERIA PASAR ACA, ACTUALMENTE HARDCODEADO

    public DeletionRequestDTO(EventDTO event, String argument, String applicantName) {
        this.event = event;
        this.argument = argument;
        this.applicantName = applicantName;
    }
}
