package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;

  private final ISourceRepository sourceRepository;

  private final ICategoryService categoryService;

  @Autowired
  public EventService(IEventRepository eventRepository, ISourceRepository sourceRepository, ICategoryService categoryService) {
    this.eventRepository = eventRepository;
    this.sourceRepository = sourceRepository;
    this.categoryService = categoryService;
  }

  @Override
  public List<EventOutputDTO> getEvents() {
    return eventRepository.findByDeleted(false).stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public EventOutputDTO createEvent(EventImportDTO eventImportDTO) {

    Source source = sourceRepository.findById(eventImportDTO.getSourceId())
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + eventImportDTO.getSourceId()));

    Category category = categoryService.findOrCreate(eventImportDTO.getCategory());

    Event event = Event.builder()
        .title(eventImportDTO.getTitle())
        .description(eventImportDTO.getDescription())
        .category(category)
        .latitude(eventImportDTO.getLatitude())
        .longitude(eventImportDTO.getLongitude())
        .eventDate(eventImportDTO.getEventDate())
        .uploadDate(LocalDateTime.now())
        .source(source)
        .build();

    eventRepository.save(event);

    return EventOutputDTO.from(event);
  }

  @Override
  public EventOutputDTO deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException("Event not found - ID: " + eventId));

    event.markAsDeleted();
    eventRepository.save(event);
    return EventOutputDTO.from(event);
  }
}
