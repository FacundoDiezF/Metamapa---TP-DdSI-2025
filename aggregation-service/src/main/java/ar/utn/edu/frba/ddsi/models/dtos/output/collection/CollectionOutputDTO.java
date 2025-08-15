package ar.utn.edu.frba.ddsi.models.dtos.output.collection;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;

public class CollectionOutputDTO {
  public String handler;

  public static CollectionOutputDTO from(Collection collection) {
    CollectionOutputDTO dto = new CollectionOutputDTO();
    dto.handler = collection.getHandler();
    return dto;
  }
}
