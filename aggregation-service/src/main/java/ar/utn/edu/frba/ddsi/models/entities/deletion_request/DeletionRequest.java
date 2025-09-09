package ar.utn.edu.frba.ddsi.models.entities.deletion_request;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(force = true)
@Table(name = "deletion_requests")
public class DeletionRequest {

  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //-- Event to delete
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "event_id", nullable = false)
  private final Event event;

  //-- Pre Evaluation info
  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "state", nullable = false)
  private DeletionRequestState state;

  @Column(name = "argument", nullable = false, columnDefinition = "TEXT")
  private final String argument;

  @Column(name = "upload_date", nullable = false)
  private final LocalDateTime uploadDate;

  @Column(name = "applicant_name", nullable = false)
  private final String applicantName; //TODO: Esto deberia ser un usuario

  //-- Post Evaluation info
  @Column(name = "evaluation_date")
  private LocalDateTime evaluationDate;

  @Column(name = "evaluator_name")
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
