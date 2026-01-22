package interfaz.grafica.ddsi.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class EditionRequestDTO {
  Long id;
  EventDTO event;

  String newTitle;
  String newDescription;
  String newCategory;
  Double newLatitude;
  Double newLongitude;
  LocalDateTime newEventDate;

  List<String> modifiedFields;
}
