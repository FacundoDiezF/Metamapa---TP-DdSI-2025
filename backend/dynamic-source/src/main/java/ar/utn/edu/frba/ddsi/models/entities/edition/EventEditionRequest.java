package ar.utn.edu.frba.ddsi.models.entities.edition;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEditionRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private EditionStatus status;

  @Column(name = "request_date", nullable = false)
  private LocalDateTime requestDate;

  // --- NEW EVENT VALUES ---

  @Column(name = "new_title")
  private String newTitle;

  @Column(name = "new_description", columnDefinition = "TEXT")
  private String newDescription;

  @ManyToOne
  @JoinColumn(name = "new_category_id")
  private Category newCategory;

  @Column(name = "new_latitude")
  private Double newLatitude;

  @Column(name = "new_longitude")
  private Double newLongitude;

  @Column(name = "new_event_date")
  private LocalDateTime newEventDate;

  // --- EVALUATION VALUES ---
  @Column(name = "evaluation_date")
  private LocalDateTime evaluationDate;

  @Column(name = "evaluator_id")
  private String evaluatorId;
}
