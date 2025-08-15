package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.models.repositories.impl.SourceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  IEventRepository eventRepository;

  ISourceRepository sourceRepository;

  @Autowired
  public EventService(IEventRepository eventRepository, SourceRepository sourceRepository) {
    this.eventRepository = eventRepository;
    this.sourceRepository = sourceRepository;
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
  public void create(EventInputDTO dto) {

    // Get event source
    Source source = sourceRepository.findByExternalIds(dto.getSourceClientId(), dto.getSourceId());
    if (source == null)
      throw new NotFoundException("Source not found for SourceClientId: " + dto.getSourceClientId() + " and sourceId: " + dto.getSourceId());

    Category category = Category // TODO: Deberiamos tener un repo y tomarla en base al "nombre"
        .builder()
        .name(dto.getCategory())
        .build();

    //Create event
    Event event = Event.builder()
        .inSourceEventId(dto.getId())
        .title(dto.getTitle())
        .description(dto.getDescription())
        .category(category)
        .latitude(dto.getLatitude())
        .longitude(dto.getLongitude())
        .eventDate(dto.getEventDate())
        .uploadDate(dto.getUploadDate())
        .source(source)
        .inSourceEventId(dto.getId())
        .build();

    source.addEvent(event);
    sourceRepository.save(source);
    eventRepository.save(event);
  }

  @Override
  public void update(Long eventId, EventInputDTO dto) {

    Event existingEvent = eventRepository.findById(eventId);
    if (existingEvent == null)
      throw new NotFoundException("Event not found with ID: " + eventId);

    Category category = Category // TODO: Deberiamos tener un repo y tomarla en base al "nombre"
        .builder()
        .name(dto.getCategory())
        .build();

    existingEvent.update(dto, category);

    eventRepository.save(existingEvent);
  }

  @Override
  public void createAll(List<EventInputDTO> dtos) {
    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)

    for (EventInputDTO dto : dtos) {
      //Validate if the event already exists in the repository
      Event event = eventRepository.findByExternalIds(dto.getSourceClientId(), dto.getSourceId(), dto.getId());
      if (event == null) {
        this.create(dto);
      } else {
        this.update(event.getId(), dto);
      }
    }
  }
}
