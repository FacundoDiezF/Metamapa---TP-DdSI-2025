package ar.utn.edu.frba.ddsi.models.entities.edition;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "edition_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventEditionRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
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

  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "field_name")
  @CollectionTable(name = "modified_fields", joinColumns = @JoinColumn(name = "edition_request_id", referencedColumnName = "id"))
  private Set<String> modifiedFields;

  // --- EVALUATION VALUES ---
  @Column(name = "evaluation_date")
  private LocalDateTime evaluationDate;

  @Column(name = "evaluator_id")
  private String evaluatorId;

  public EventEditionRequest(Event originalEvent, EventDTO changes, Category resolvedNewCategory) {

    // MetaData
    this.event = originalEvent;
    this.status = EditionStatus.PENDING;
    this.requestDate = LocalDateTime.now();
    this.modifiedFields = new HashSet<>();

    // New Values (We save all as an snapshot)
    this.newTitle = changes.getTitle();
    this.newDescription = changes.getDescription();
    this.newCategory = resolvedNewCategory;
    this.newLatitude = changes.getLatitude();
    this.newLongitude = changes.getLongitude();
    this.newEventDate = changes.getEventDate().atStartOfDay();

    // Detect Changes
    detectAllChanges(originalEvent);
  }

  private void detectAllChanges(Event original) {
    checkChange("title", original.getTitle(), this.newTitle);
    checkChange("description", original.getDescription(), this.newDescription);
    checkChange("category", original.getCategory(), this.newCategory);
    checkChange("latitude", original.getLatitude(), this.newLatitude);
    checkChange("longitude", original.getLongitude(), this.newLongitude);
    checkChange("eventDate", original.getEventDate(), this.newEventDate);
  }

  private void checkChange(String fieldName, Object originalValue, Object newValue) {
    // Si son diferentes, agregamos el campo a la lista
    if (!Objects.equals(originalValue, newValue)) {
      this.modifiedFields.add(fieldName);
    }
  }

  public void applyChangesToEvent() {
    if (this.status != EditionStatus.APPROVED) {
      throw new IllegalStateException("Cannot apply changes from a request that is not APPROVED");
    }

    // Iterates over modified fields and applies changes to the target event
    for (String field : this.modifiedFields) {
      switch (field) {
        case "title" -> event.setTitle(this.newTitle);
        case "description" -> event.setDescription(this.newDescription);
        case "category" -> event.setCategory(this.newCategory);
        case "latitude" -> event.setLatitude(this.newLatitude);
        case "longitude" -> event.setLongitude(this.newLongitude);
        case "eventDate" -> event.setEventDate(this.newEventDate);
      }
    }

    // Set the update date of the event
    event.setUpdateDate(LocalDateTime.now());
  }

  public void evaluate(EditionStatus newStatus, String evaluatorId) {
    this.status = newStatus;
    this.evaluatorId = evaluatorId;
    this.evaluationDate = LocalDateTime.now();
  }

}
