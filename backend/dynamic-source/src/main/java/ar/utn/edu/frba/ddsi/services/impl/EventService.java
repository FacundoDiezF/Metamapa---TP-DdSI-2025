package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.exceptions.UnauthorizedException;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.edition.EditionStatus;
import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.repositories.IEditionRequestRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IEditionRequestService;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;

  private final ISubmissionRepository submissionRepository;

  private final ICategoryService categoryService;

  private final IEditionRequestRepository editionRequestRepository;

  @Autowired
  public EventService(IEventRepository eventRepository, ISubmissionRepository submissionRepository, ICategoryService categoryService, IEditionRequestRepository editionRequestRepository) {
    this.eventRepository = eventRepository;
    this.submissionRepository = submissionRepository;
    this.categoryService = categoryService;
    this.editionRequestRepository = editionRequestRepository;
  }

  @Override
  public List<EventOutputDTO> getAllEvents() {
    return eventRepository.findByAccepted().stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public EventOutputDTO save(EventDTO dto) {
    //TODO: Validar si el usuario puede realizar esta peticion

    Category category = categoryService.findOrCreate(dto.getCategory());

    Event event = Event.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .category(category)
        .latitude(dto.getLatitude())
        .longitude(dto.getLongitude())
        .eventDate(dto.getEventDate().atStartOfDay())
        .uploadDate(LocalDateTime.now())
        .contributor(dto.getContributor()) //TODO: Esto deberia ser un usuario
        .build();

    eventRepository.save(event);
    submissionRepository.save(new SubmissionRequest(event));

    return EventOutputDTO.from(event);
  }

  @Override
  public void update(Long eventId, EventDTO dto) {

    //TODO: Validar si el usuario puede realizar esta peticion

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException("Event not found - Id: " + eventId));

    if (!event.getContributor().equals(dto.getContributor()))
      throw new UnauthorizedException("Unauthorized action: event ownership mismatch");

    if (event.getUploadDate().plusDays(7).isBefore(LocalDateTime.now()))
      throw new IllegalStateException("Event editing window has expired. Modifications are no longer allowed");

    if (!event.isAccepted())
      throw new IllegalStateException("Only accepted events can be edited");

    Category category = categoryService.findOrCreate(dto.getCategory());

    EventEditionRequest newEditionRequest = EventEditionRequest.builder()
        .event(event)
        .status(EditionStatus.PENDING)
        .requestDate(LocalDateTime.now())
        .newTitle(dto.getTitle())
        .newDescription(dto.getDescription())
        .newCategory(category)
        .newLatitude(dto.getLatitude())
        .newLongitude(dto.getLongitude())
        .newEventDate(dto.getEventDate().atStartOfDay())
        .build();

    editionRequestRepository.save(newEditionRequest);
  }

  @Override
  public EventOutputDTO deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException("Event not found - Id: " + eventId));

    event.markAsDeleted();
    eventRepository.save(event);

    return EventOutputDTO.from(event);
  }
}
