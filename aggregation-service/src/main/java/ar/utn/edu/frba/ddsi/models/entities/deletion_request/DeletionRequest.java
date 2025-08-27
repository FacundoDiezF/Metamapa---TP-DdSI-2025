package ar.utn.edu.frba.ddsi.models.entities.deletion_request;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class DeletionRequest {

  @Setter
  private Long id;
  private final Event event;

  private final String argument;
  @Setter
  private DeletionRequestState state;

  private final LocalDateTime uploadDate;
  private LocalDateTime evaluationDate;

  private final String applicantName; //TODO: Esto deberia ser un usuario
  private String evaluatorName; //TODO: Esto deberia ser un usuario

  public static DeletionRequest from(DeletionRequestCreationDTO dto, Event event) {
    return new DeletionRequest(event, dto.getArgument(), dto.getApplicantName());
  }

  public DeletionRequest(Event event, String argument, String applicantName) {

    if (!this.isArgumentValid(argument))
      throw new IllegalArgumentException("Argument must contain a minimum of 500 characters. Now it has: " + argument.length());

    this.event = event;
    this.argument = argument;
    this.state = DeletionRequestState.PENDING;
    this.uploadDate = LocalDateTime.now();
    this.applicantName = applicantName;
  }

  private boolean isArgumentValid(String argument) {
    return argument.length() >= 500;
  }

  public void evaluate(boolean accepted, String evaluatorName) {

    this.registerEvaluation(evaluatorName);

    if (accepted) {
      event.markAsDeleted();
      state = DeletionRequestState.ACCEPTED;
    } else {
      state = DeletionRequestState.REJECTED;
    }
  }


  private void registerEvaluation(String evaluatorName) {
    this.evaluationDate = LocalDateTime.now();
    this.evaluatorName = evaluatorName;
  }
}
