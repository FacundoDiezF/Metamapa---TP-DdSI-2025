package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {
  List<EventOutputDTO> getAllEvents(LocalDateTime lastUpdate);

  List<Event> getEvents(LocalDateTime lastUpdate);

  EventOutputDTO save(EventDTO dto);

  EventOutputDTO update(Long eventId, EventDTO dto);

  EventOutputDTO deleteEvent(Long eventId);

  List<EventOutputDTO> getPendingEvents();
}