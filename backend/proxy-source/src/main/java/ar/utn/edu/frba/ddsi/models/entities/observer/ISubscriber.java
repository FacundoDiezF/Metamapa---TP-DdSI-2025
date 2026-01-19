package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;

import java.util.List;
import reactor.core.publisher.Mono;

public interface ISubscriber {
  Mono<Void> notifyNewEvents(List<EventOutputDTO> events);

  Mono<Void> notifyUpdatedEvents(List<EventOutputDTO> events);

  Mono<Void> notifySource(Source source);
}
