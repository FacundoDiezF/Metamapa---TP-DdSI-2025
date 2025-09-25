package ar.utn.edu.frba.ddsi.models.entities.submission;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(force = true)
@Table(name = "submission_requests")
public class SubmissionRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "event_id", nullable = false, unique = true)
  private final Event event;

  //-- Post Evaluation
  @Column(name = "evaluation_date")
  private LocalDateTime evaluationDate;

  @Column(name = "reviewer")
  private String reviewer; //TODO: Esto deberia ser un usuario

  @Column(name = "state")
  @Enumerated(EnumType.STRING)
  private SubmissionState state;

  @Column(name = "suggestion")
  private String suggestion;

  public SubmissionRequest(Event event) {
    this.event = event;
    this.state = SubmissionState.PENDING;
  }

  public void setEvaluation(SubmissionEvaluationDTO submissionEvaluation) {
    this.evaluationDate = LocalDateTime.now();
    this.reviewer = submissionEvaluation.getReviewer();
    this.state = submissionEvaluation.getState();
    this.suggestion = submissionEvaluation.getSuggestion();

    if (this.isAccepted()) {
      event.markAsAccepted();
    }
  }

  public boolean isAccepted() {
    return state == SubmissionState.ACCEPTED || state == SubmissionState.ACCEPTED_WITH_SUGGESTIONS;
  }

  public boolean isPending() {
    return state == SubmissionState.PENDING;
  }
}
