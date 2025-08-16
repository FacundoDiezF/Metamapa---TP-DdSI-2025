package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;

import java.util.List;

public interface ISubscriber {
  void notifyNewEvent(List<Event> event);
  void notifyUpdatedEvent(Event event);
  void notifySource(SourceOutputDTO source);
}
