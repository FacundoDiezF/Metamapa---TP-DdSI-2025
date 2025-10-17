package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;

public interface ISubscriber {
  void notifyNewEvent(List<EventOutputDTO> event);

  void notifyUpdatedEvent(EventOutputDTO event);

  void notifySource(SourceOutputDTO source);
}
