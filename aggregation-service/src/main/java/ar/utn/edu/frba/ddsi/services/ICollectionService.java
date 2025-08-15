package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface ICollectionService {
  void create(CollectionCreationDTO collectionDto);

  List<CollectionOutputDTO> getCollections();

  List<EventOutputDTO> getEventsFromCollection(
      String handler,
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  );

  void refreshCollections(LocalDateTime lastUpdate);
}
