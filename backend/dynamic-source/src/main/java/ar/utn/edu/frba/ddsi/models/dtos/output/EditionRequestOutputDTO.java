package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class EditionRequestOutputDTO {
  Long id;
  EventOutputDTO event;

  String newTitle;
  String newDescription;
  String newCategory;
  Double newLatitude;
  Double newLongitude;
  LocalDateTime newEventDate;

  List<String> modifiedFields;

  public EditionRequestOutputDTO(Long id, EventOutputDTO event, String newTitle, String newDescription, String newCategory, Double newLatitude, Double newLongitude, LocalDateTime newEventDate, List<String> modifiedFields) {
    this.id = id;
    this.event = event;
    this.newTitle = newTitle;
    this.newDescription = newDescription;
    this.newCategory = newCategory;
    this.newLatitude = newLatitude;
    this.newLongitude = newLongitude;
    this.newEventDate = newEventDate;
    this.modifiedFields = modifiedFields;
  }

  public static EditionRequestOutputDTO from(EventEditionRequest request) {
    Event original = request.getEvent();
    List<String> changes = new ArrayList<>();

    // Detect and send changes to simplify styling in frontend
    if (!Objects.equals(original.getTitle(), request.getNewTitle())) {
      changes.add("title");
    }
    if (!Objects.equals(original.getDescription(), request.getNewDescription())) {
      changes.add("description");
    }
    if (!Objects.equals(original.getCategory(), request.getNewCategory())) {
      changes.add("category");
    }
    if (!Objects.equals(original.getLatitude(), request.getNewLatitude())) {
      changes.add("latitude");
    }
    if (!Objects.equals(original.getLongitude(), request.getNewLongitude())) {
      changes.add("longitude");
    }
    if (!Objects.equals(original.getEventDate(), request.getNewEventDate())) {
      changes.add("eventDate");
    }

    return new EditionRequestOutputDTO(
        request.getId(),
        EventOutputDTO.from(original),
        request.getNewTitle(),
        request.getNewDescription(),
        request.getNewCategory() != null ? request.getNewCategory().getName() : null,
        request.getNewLatitude(),
        request.getNewLongitude(),
        request.getNewEventDate(),
        changes
    );
  }
}
