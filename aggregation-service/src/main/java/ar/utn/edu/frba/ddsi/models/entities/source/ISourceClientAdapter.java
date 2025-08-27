package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.util.List;

public interface ISourceClientAdapter {

  Long getId();

  void subscribe(String callbackUrl);

  List<EventInputDTO> fetchEventsBySource(Source source);

  void deleteEvent(Event event);

}