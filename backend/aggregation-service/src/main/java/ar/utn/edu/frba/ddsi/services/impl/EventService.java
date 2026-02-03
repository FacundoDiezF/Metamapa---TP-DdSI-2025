package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.MultimediaInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.entities.strategies.UrlGenerationStrategy;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import ar.utn.edu.frba.ddsi.services.INormalizationService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;

  private final ISourceRepository sourceRepository;

  private final ICategoryService categoryService;

  private final ILocationService locationService;

  private final INormalizationService normalizationService;

  private final UrlGenerationStrategy urlGenerationStrategy;

  @Autowired
  public EventService(
      IEventRepository eventRepository,
      ISourceRepository sourceRepository,
      ICategoryService categoryService,
      ILocationService locationService,
      INormalizationService normalizationService,
      UrlGenerationStrategy urlGenerationStrategy){
    this.eventRepository = eventRepository;
    this.sourceRepository = sourceRepository;
    this.categoryService = categoryService;
    this.locationService = locationService;
    this.normalizationService = normalizationService;
    this.urlGenerationStrategy = urlGenerationStrategy;
  }

  @Override
  public Page<EventOutputDTO> getEvents(
      List<Long> category_in,
      List<Long> province_in,
      List<Origin> source_in,
      LocalDateTime uploadDate_lt,
      LocalDateTime uploadDate_gt,
      LocalDateTime eventDate_lt,
      LocalDateTime eventDate_gt,
      LocalDateTime fromUpdatedDate,
      Pageable pageable
  ) {
    Page<Event> eventsPage = eventRepository.findFiltered(category_in, province_in, source_in, uploadDate_lt, uploadDate_gt, eventDate_lt, eventDate_gt, fromUpdatedDate, pageable);
    return eventsPage.map(event -> EventOutputDTO.from(event, urlGenerationStrategy));
  }

  @Override
  public void createAll(List<EventInputDTO> dtos, Long sourceClientId) {
    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)
    //1. Build events from dtos
    List<Event> events = dtos
        .stream()
        .map(dto -> {
          dto.setSourceClientId(sourceClientId);
          return buildEventFromDTO(dto);
        })
        .toList();

    //2. Normalize locations
    //TODO pensar como manejar el caso en el que falla la API, si guardar eventos sin normalizar o que (y como manejar el error
    List<Event> normalizedEvents = normalizationService.eventLocationNormalization(events);

    //3. Save events
    eventRepository.saveAll(normalizedEvents); // TODO: Que se guarde en cascada la source al guardar el evento
  }

  @Override
  public void updateAll(List<EventInputDTO> dtos, Long sourceClientId) {
    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)
    dtos.forEach(dto -> {
      dto.setSourceClientId(sourceClientId);
      this.update(dto);
    });
  }

  private void update(EventInputDTO dto) {

    Event event = eventRepository.findByExternalIds(dto.getSourceClientId(), dto.getSourceId(), dto.getId())
        .orElseThrow(() -> new NotFoundException("Event not found for SourceClientId: " + dto.getSourceClientId() + " and sourceId: " + dto.getSourceId() + " and eventId: " + dto.getId()));

    // Get or create solicited category
    Category category = categoryService.findOrCreate(dto.getCategory());

    // Update location if changed
    Coordinate newCoords = new Coordinate(dto.getLatitude(), dto.getLongitude());
    if (!event.getLocation().getCoordinate().equals(newCoords)) {
      Location location = locationService.findOrCreateLocationByCoordinates(Set.of(newCoords)).get(0);
      event.setLocation(location); //TODO: A mejorar
    }

    // Update other fields (name, description, category, eventDate)
    event.update(dto, category);

    eventRepository.save(event);
  }

  private Event buildEventFromDTO(EventInputDTO dto) {

    // Get event source
    Source source = sourceRepository.findByExternalIds(dto.getSourceClientId(), dto.getSourceId())
        .orElseThrow(() -> new NotFoundException("Source not found for SourceClientId: " + dto.getSourceClientId() + " and sourceId: " + dto.getSourceId()));

    Category category = categoryService.findOrCreate(dto.getCategory());

    Location location = locationService.findOrCreate(dto.getLatitude(), dto.getLongitude());

    //Create event
    Event event = Event.builder()
        .originEventId(dto.getId())
        .title(dto.getTitle())
        .description(dto.getDescription())
        .category(category)
        .location(location)
        .eventDate(dto.getEventDate())
        .uploadDate(dto.getUploadDate())
        .source(source)
        .build();

    List<Multimedia> resources = buildMultimediaFromDTOs(dto.getResources());

    event.addResources(resources);

    //Update source
    source.addEvent(event);

    return event;
  }

  private List<Multimedia> buildMultimediaFromDTOs(List<MultimediaInputDTO> multimediaDTOs) {
    return multimediaDTOs
        .stream().
        map(dto ->
            Multimedia.builder()
            .objectStoragePath(dto.getResourcePath())
            .name(dto.getName())
            .mimeType(dto.getMimeType())
            .build())
        .toList();
  }
}
