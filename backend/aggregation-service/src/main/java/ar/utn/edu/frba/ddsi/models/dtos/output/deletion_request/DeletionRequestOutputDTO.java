package ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequestState;
import lombok.Data;

@Data
public class DeletionRequestOutputDTO {
  Long id;
  Long eventId;
  String argument;
  DeletionRequestState state;

  public DeletionRequestOutputDTO(Long id, Long eventId, String argument, DeletionRequestState state) {
    this.id = id;
    this.eventId = eventId;
    this.argument = argument;
    this.state = state;
  }

  public static DeletionRequestOutputDTO from(DeletionRequest dr) {
    return new DeletionRequestOutputDTO(dr.getId(), dr.getEvent().getId(), dr.getArgument(), dr.getState());
  }
}
