package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionUpdateDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionCriteriaService;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CollectionService implements ICollectionService {

  private final ICollectionRepository collectionRepository;

  private final ISourceRepository sourceRepository;

  private final ICollectionCriteriaService collectionCriteriaService;

  @Autowired
  public CollectionService(ICollectionRepository collectionRepository, ISourceRepository sourceRepository, ICollectionCriteriaService collectionCriteriaService) {
    this.collectionRepository = collectionRepository;
    this.sourceRepository = sourceRepository;
    this.collectionCriteriaService = collectionCriteriaService;
  }

  @Override
  public CollectionOutputDTO create(CollectionCreationDTO collectionDto) {

    // Get sources from the repository by IDs
    Set<Source> collectionSources = new HashSet<>(sourceRepository.findAllById(collectionDto.getSourceIds()));

    //TODO: validar que todas las fuentes solicitadas existan, tirar error en caso contrario

    //Construct CollectionCriteria from DTO
    CollectionCriteria collectionCriteria = collectionCriteriaService.findOrCreate(collectionDto.getConditions());

    // Construct Collection
    Collection collection = Collection.builder()
        .title(collectionDto.getTitle())
        .description(collectionDto.getDescription())
        .sources(collectionSources)
        .collectionCriteria(collectionCriteria)
        .consensusType(collectionDto.getConsensusType())
        .build();

    //Refresh collection to populate events
    collection.refresh(null);
    collectionRepository.save(collection);

    return CollectionOutputDTO.from(collection);
  }

  @Override
  public Page<CollectionOutputDTO> getCollections(LocalDateTime fromUpdateDate, Pageable pageable) {
    Page<Collection> collections = fromUpdateDate != null
        ? collectionRepository.findAllByUpdatedDateAfter(fromUpdateDate, pageable)
        : collectionRepository.findAll(pageable);
    return collections.map(CollectionOutputDTO::from);
  }

  @Override
  public List<EventOutputDTO> getEventsFromCollection(
      boolean curedNavigation,
      String handler,
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  ) {

    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);


    List<Event> finalEvents = collection.getEvents().stream()
        .filter(e -> (!curedNavigation || e.meetsAlgorithmConsensus(collection.getConsensusType())))
        .filter(e -> (category == null || e.getCategory().equals(new Category(category))))
        .filter(e -> (untilUploadDate == null || e.getUploadDate().isBefore(untilUploadDate)))
        .filter(e -> (fromUploadDate == null || e.getUploadDate().isAfter(fromUploadDate)))
        .filter(e -> (untilEventDate == null || e.getEventDate().isBefore(untilEventDate)))
        .filter(e -> (fromEventDate == null || e.getEventDate().isAfter(fromEventDate)))
        .toList();

    return finalEvents.stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public void refreshCollections(LocalDateTime lastUpdate) {
    for (Collection collection : collectionRepository.findAll()) {
      collection.refresh(lastUpdate);
      collectionRepository.save(collection);
    }
  }

  @Override
  public void updateCollection(String handler, CollectionUpdateDTO dto) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    collection.update(dto);

    collectionRepository.save(collection);
  }

  @Override
  public void deleteCollection(String handler) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler); //TODO Validar
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    collectionRepository.delete(collection);
  }

  @Override
  public CollectionOutputDTO getCollectionByHandler(String handler) {
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    return CollectionOutputDTO.from(collection);
  }

  @Override
  public void updateConsensus(String handler, ConsensusType consensusType) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);
    collection.setConsensusType(consensusType);
    collectionRepository.save(collection);
  }

  @Override
  public void removeSource(String handler, Long sourceId) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    Source source = sourceRepository.findById(sourceId)
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + sourceId));

    collection.remove(source);

    collectionRepository.save(collection);
  }

  @Override
  public void addSource(String handler, Long sourceId) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    Source source = sourceRepository.findById(sourceId)
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + sourceId));

    collection.add(source);
    collectionRepository.save(collection);
  }
}

