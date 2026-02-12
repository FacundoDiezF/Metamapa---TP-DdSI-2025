package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICollectionService {
  CollectionOutputDTO create(CollectionCreationDTO collectionDto);

  Page<CollectionOutputDTO> getCollections(LocalDateTime fromUpdateDate, Pageable pageable);

  List<EventOutputDTO> getEventsFromCollection(
      String handler,
      boolean curedNavigation,
      List<Long> category_in,
      List<Long> province_in,
      List<Origin> source_in,
      LocalDateTime uploadDate_lt,
      LocalDateTime uploadDate_gt,
      LocalDateTime eventDate_lt,
      LocalDateTime eventDate_gt
  );

  void refreshCollections(LocalDateTime lastUpdate);

  void updateCollection(String handler, CollectionCreationDTO dto);

  void deleteCollection(String handler);

  CollectionOutputDTO getCollectionByHandler(String handler);

  void updateConsensus(String handler, ConsensusType consensusType);

  void removeSource(String handler, Long sourceId);

  void addSource(String handler, Long sourceId);
}
