package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import java.time.LocalDateTime;
import java.util.Set;
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
  Set<String> modifiedFields;

  public EditionRequestOutputDTO(Long id, EventOutputDTO event, String newTitle, String newDescription, String newCategory, Double newLatitude, Double newLongitude, LocalDateTime newEventDate, Set<String> modifiedFields) {
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
    return new EditionRequestOutputDTO(
        request.getId(),
        EventOutputDTO.from(request.getEvent()),
        request.getNewTitle(),
        request.getNewDescription(),
        request.getNewCategory() != null ? request.getNewCategory().getName() : null,
        request.getNewLatitude(),
        request.getNewLongitude(),
        request.getNewEventDate(),
        request.getModifiedFields()
    );
  }
}
