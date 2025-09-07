package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import java.util.List;

public interface IEventService {
  List<EventOutputDTO> getAllEvents();

  EventOutputDTO save(EventDTO dto);

  EventOutputDTO update(Long eventId, EventDTO dto);

  EventOutputDTO deleteEvent(Long eventId);
}