package interfaz.grafica.ddsi.dtos;

import java.util.List;
import lombok.Data;

@Data
public class CollectionDTO {
  String handler;
  String title;
  String description;
  List<SourceDTO> sources;
  List<Long> eventIds;

  public CollectionDTO(String handler, String title, String description, List<SourceDTO> sources, List<Long> eventIds) {
    this.handler = handler;
    this.title = title;
    this.description = description;
    this.sources = sources;
    this.eventIds = eventIds;
  }
}
