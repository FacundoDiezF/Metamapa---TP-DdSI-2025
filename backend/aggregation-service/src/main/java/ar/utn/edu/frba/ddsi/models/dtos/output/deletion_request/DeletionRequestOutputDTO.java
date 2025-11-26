package ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request;

import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequestState;
import lombok.Data;

@Data
public class DeletionRequestOutputDTO {
  Long id;
  EventOutputDTO event;
  String argument;
  DeletionRequestState state;

  public DeletionRequestOutputDTO(Long id, EventOutputDTO event, String argument, DeletionRequestState state) {
    this.id = id;
    this.event = event;
    this.argument = argument;
    this.state = state;
  }

  public static DeletionRequestOutputDTO from(DeletionRequest dr) {
    return new DeletionRequestOutputDTO(
        dr.getId(),
        EventOutputDTO.from(dr.getEvent()),
        dr.getArgument(),
        dr.getState());
  }
}
