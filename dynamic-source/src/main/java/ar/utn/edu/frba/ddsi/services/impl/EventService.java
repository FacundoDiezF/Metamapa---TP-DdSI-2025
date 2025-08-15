package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.exceptions.UnauthorizedException;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  IEventRepository eventRepository;

  ISubmissionRepository submissionRepository;

  Publisher publisher;

  public EventService(IEventRepository eventRepository, ISubmissionRepository submissionRepository, Publisher publisher) {
    this.eventRepository = eventRepository;
    this.submissionRepository = submissionRepository;
    this.publisher = publisher;
  }

  @Override
  public List<EventOutputDTO> getAllEvents(LocalDateTime lastUpdate) {

    return getEvents(lastUpdate).stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public List<Event> getEvents(LocalDateTime lastUpdate) {
    if (lastUpdate != null) {
      return eventRepository.findAfterDate(lastUpdate); //Incluye dentro findByAccepted()
    }
    return eventRepository.findByAccepted();
  }

  @Override
  public EventOutputDTO save(EventDTO dto) {
    //TODO: Validar si el usuario puede realizar esta peticion

    Event eventSaved = eventRepository.save(Event.from(dto));
    submissionRepository.save(new SubmissionRequest(eventSaved));

    return EventOutputDTO.from(eventSaved);
  }

  @Override
  public EventOutputDTO update(Long eventId, EventDTO dto) {
    //TODO: Validar si el usuario puede realizar esta peticion
    Event event = eventRepository.findById(eventId);

    if (event == null)
      throw new NotFoundException("Event not found - ID: " + eventId);

    if (!event.getContributor().equals(dto.getContributor()))
      throw new UnauthorizedException("Unauthorized action: event ownership mismatch");

    if (event.getUploadDate().plusDays(7).isBefore(LocalDateTime.now()))
      throw new IllegalStateException("Event editing window has expired. Modifications are no longer allowed");

    event.update(dto);
    Event eventSaved = eventRepository.save(event);

    if (eventSaved.isAccepted()) {
      publisher.notifyUpdatedEvents(eventSaved);
    }

    return EventOutputDTO.from(eventSaved);
  }

  @Override
  public EventOutputDTO deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId);
    if (event == null) throw new NotFoundException("Event not found - Id: " + eventId);

    event.markAsDeleted();

    Event deletedEvent = eventRepository.delete(event);

    return EventOutputDTO.from(deletedEvent);
  }

  @Override
  public List<EventOutputDTO> getPendingEvents() {
    return submissionRepository.findPendingSubmissions()
        .stream().map(SubmissionRequest::getEvent)
        .map(EventOutputDTO::from)
        .toList();
  }
}
