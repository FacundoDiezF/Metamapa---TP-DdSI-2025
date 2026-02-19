package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.entities.strategies.UrlGenerationStrategy;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionCriteriaService;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import jakarta.transaction.Transactional;
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

  private final IEventRepository eventRepository;

  private final ICollectionCriteriaService collectionCriteriaService;

  private final UrlGenerationStrategy urlGenerationStrategy;

  @Autowired
  public CollectionService(ICollectionRepository collectionRepository,
                           ISourceRepository sourceRepository, IEventRepository eventRepository,
                           ICollectionCriteriaService collectionCriteriaService,
                           UrlGenerationStrategy urlGenerationStrategy) {
    this.collectionRepository = collectionRepository;
    this.sourceRepository = sourceRepository;
    this.eventRepository = eventRepository;
    this.collectionCriteriaService = collectionCriteriaService;
    this.urlGenerationStrategy = urlGenerationStrategy;
  }

  @Override
  public CollectionOutputDTO create(CollectionCreationDTO collectionDto) {

    // Get sources from the repository by IDs
    Set<Source> collectionSources = new HashSet<>(sourceRepository.findAllById(collectionDto.getSourceIds()));

    //TODO: validar que todas las fuentes solicitadas existan, tirar error en caso contrario

    //Construct CollectionCriteria from DTO
    CollectionCriteria collectionCriteria = collectionCriteriaService.findOrCreate(collectionDto.getConditions());

    // Construct Collection
    Collection collection = Collection.builder().title(collectionDto.getTitle()).description(collectionDto.getDescription()).sources(collectionSources).collectionCriteria(collectionCriteria).consensusType(collectionDto.getConsensusType()).build();

    //Refresh collection to populate events
    collection.refresh(null);
    collectionRepository.save(collection);

    return CollectionOutputDTO.from(collection);
  }

  @Override
  public Page<CollectionOutputDTO> getCollections(LocalDateTime fromUpdateDate, Pageable pageable) {
    Page<Collection> collections = fromUpdateDate != null ? collectionRepository.findAllByUpdatedDateAfter(fromUpdateDate, pageable) : collectionRepository.findAll(pageable);
    return collections.map(CollectionOutputDTO::from);
  }

  @Override
  public Page<EventOutputDTO> getEventsFromCollection(String handler, boolean curedNavigation, List<Long> category_in, List<Long> province_in, List<Origin> source_in, LocalDateTime uploadDate_lt, LocalDateTime uploadDate_gt, LocalDateTime eventDate_lt, LocalDateTime eventDate_gt, Pageable pageable) {

    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    ConsensusType consensusFilter = (!curedNavigation || collection.getConsensusType() == ConsensusType.ALL_PASS) ? null : collection.getConsensusType();

    // Get page from event repositorie
    Page<Event> fromDataBasePage = eventRepository.findByCollectionHandlerFiltered(collection.getHandler(), consensusFilter, category_in, province_in, source_in, uploadDate_lt, uploadDate_gt, eventDate_lt, eventDate_gt, pageable);

    //TODO: Agregar metamapa (Hacer peticion filtrada y unificar resultados con los de la base de datos)

    return fromDataBasePage.map(event -> EventOutputDTO.from(event, urlGenerationStrategy));
  }

  @Override
  @Transactional
  public void refreshCollections(LocalDateTime lastUpdate) {
    for (Collection collection : collectionRepository.findAll()) {
      collection.refresh(lastUpdate);
      collectionRepository.save(collection);
    }
  }

  @Override
  @Transactional
  public void updateCollection(String handler, CollectionCreationDTO dto) {

    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    List<Source> foundSources = sourceRepository.findAllById(dto.getSourceIds());
    if (foundSources.size() != dto.getSourceIds().size()) {
      throw new NotFoundException("Una o más fuentes no existen.");
    }
    Set<Source> newSources = new HashSet<>(foundSources);

    // Collection criteria update check (from the DTOs)
    CollectionCriteria criteriaToUpdate = collection.getCollectionCriteria();
    if (dto.getConditions() != null) {
      List<ConditionDTO> currentConditionsAsDTOs = collection.getCollectionCriteria().getConditions().stream().map(ConditionDTO :: from).toList();

      // Use Set to ignore order and duplicates
      Set<ConditionDTO> currentSet = new HashSet<>(currentConditionsAsDTOs);
      Set<ConditionDTO> newSet = new HashSet<>(dto.getConditions());

      // Creates new condition if there is any change
      if (!currentSet.equals(newSet)) {
        criteriaToUpdate = collectionCriteriaService.findOrCreate(dto.getConditions());
      }
    }

    collection.update(dto, newSources, criteriaToUpdate);

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

  //! ------------------------ Metodos que actualmente estan al pedo ------------------------ \\

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

    Source source = sourceRepository.findById(sourceId).orElseThrow(() -> new NotFoundException("Source not found - ID: " + sourceId));

    collection.remove(source, true);

    collectionRepository.save(collection);
  }

  @Override
  public void addSource(String handler, Long sourceId) {
    //TODO: validate that user can do this petition.
    Collection collection = collectionRepository.findByHandler(handler);
    if (collection == null) throw new NotFoundException("Collection not found - Handler: " + handler);

    Source source = sourceRepository.findById(sourceId).orElseThrow(() -> new NotFoundException("Source not found - ID: " + sourceId));

    collection.add(source, true);
    collectionRepository.save(collection);
  }
}

