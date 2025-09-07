package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import java.util.List;

public interface IEventService {

  List<EventOutputDTO> getEvents();

  EventOutputDTO createEvent(EventImportDTO eventImportDTO);

  EventOutputDTO deleteEvent(Long eventId);
}
