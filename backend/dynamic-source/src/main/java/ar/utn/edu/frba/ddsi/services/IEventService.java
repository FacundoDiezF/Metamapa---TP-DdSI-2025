package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import java.util.List;

public interface IEventService {
  List<EventOutputDTO> getAllEvents();

  List<EventOutputDTO> getEventsByUserId(String userId);

  EventOutputDTO save(EventDTO dto, String userId);

  void update(String userId, Long eventId, EventDTO dto);

  EventOutputDTO deleteEvent(Long eventId);
}