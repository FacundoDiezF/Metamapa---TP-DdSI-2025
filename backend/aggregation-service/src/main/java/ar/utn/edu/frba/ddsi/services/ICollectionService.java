package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionUpdateDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ICollectionService {
  CollectionOutputDTO create(CollectionCreationDTO collectionDto);

  Page<CollectionOutputDTO> getCollections(LocalDateTime fromUpdateDate, Pageable pageable);

  List<EventOutputDTO> getEventsFromCollection(
      boolean curedNavigation,
      String handler,
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  );

  void refreshCollections(LocalDateTime lastUpdate);

  void updateCollection(String handler, CollectionUpdateDTO dto);

  void deleteCollection(String handler);

  CollectionOutputDTO getCollectionByHandler(String handler);

  void updateConsensus(String handler, ConsensusType consensusType);

    void removeSource(String handler, Long sourceId);

    void addSource(String handler, Long sourceId);
}
