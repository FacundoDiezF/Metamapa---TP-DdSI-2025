package ar.utn.edu.frba.ddsi.models.dtos.output.collection;

import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import lombok.Data;

import java.util.List;

@Data
public class CollectionOutputDTO {

  String handler;
  String title;
  String description;
  List<SourceOutputDTO> sources;


  public static CollectionOutputDTO from(Collection collection) {
    CollectionOutputDTO dto = new CollectionOutputDTO();
    dto.handler = collection.getHandler();
    dto.title = collection.getTitle();
    dto.description = collection.getDescription();
    dto.sources = collection.getSources().stream().map(SourceOutputDTO::from).toList();
    return dto;
  }
}
