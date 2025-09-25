package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.observer.Aggregator;
import ar.utn.edu.frba.ddsi.models.entities.observer.ISubscriber;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import ar.utn.edu.frba.ddsi.services.ISubscriberService;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService implements ISubscriberService {

  private final Publisher publisher;

  private final IEventService eventService;

  private final ISourceService sourceService;

  public SubscriberService(Publisher publisher,
                           IEventService eventService,
                           ISourceService sourceService) {
    this.publisher = publisher;
    this.eventService = eventService;
    this.sourceService = sourceService;
  }

  @Override
  public void subscribe(SubscriberInputDTO dto) {
    ISubscriber subscriber = Aggregator.from(dto);

    publisher.subscribe(subscriber);

    // Notificarle TODAS las sources actuales SOLO al nuevo subscriber
    sourceService.getSources().forEach(subscriber::notifySource);

    // Notificarle TODOS los eventos actuales SOLO al nuevo subscriber
    subscriber.notifyNewEvent(eventService.getAllEvents());
  }
}
