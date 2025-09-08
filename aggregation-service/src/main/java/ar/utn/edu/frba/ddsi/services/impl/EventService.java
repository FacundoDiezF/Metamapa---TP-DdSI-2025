package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;

  private final ISourceRepository sourceRepository;

  private final ICategoryService categoryService;

  private final ILocationService locationService;

  @Autowired
  public EventService(IEventRepository eventRepository, ISourceRepository sourceRepository, ICategoryService categoryService, ILocationService locationService) {
    this.eventRepository = eventRepository;
    this.sourceRepository = sourceRepository;
    this.categoryService = categoryService;
    this.locationService = locationService;
  }

  @Override
  public List<EventOutputDTO> getEvents(
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate) {
    return eventRepository.findFiltered(category, untilUploadDate, fromUploadDate, untilEventDate, fromEventDate).stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public void createAll(List<EventInputDTO> dtos) {
    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)
    dtos.forEach(this::create);
  }

  @Override
  public void updateAll(List<EventInputDTO> dtos) {
    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)
    dtos.forEach(this::update);
  }

  private void create(EventInputDTO dto) {

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

    source.addEvent(event);

    sourceRepository.save(source);
    eventRepository.save(event); //? Manejar cascade
  }

  private void update(EventInputDTO dto) {

    Event event = eventRepository.findByExternalIds(dto.getSourceClientId(), dto.getSourceId(), dto.getId())
        .orElseThrow(() -> new NotFoundException("Event not found for SourceClientId: " + dto.getSourceClientId() + " and sourceId: " + dto.getSourceId() + " and eventId: " + dto.getId()));

    Category category = categoryService.findOrCreate(dto.getCategory());

    event.update(dto, category);

    eventRepository.save(event);
  }
}
