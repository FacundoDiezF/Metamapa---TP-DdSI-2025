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

  Publisher publisher;

  IEventService eventService;

  ISourceService sourceService;

  public SubscriberService(Publisher publisher,
                           IEventService eventService,
                           ISourceService sourceService) {
    this.publisher = publisher;
    this.eventService = eventService;
    this.sourceService = sourceService;
  }

  @Override
  public void subscribe(SubscriberInputDTO dto) {
    //A futuro necesitariamos un builder si hay otros tipos de suscribers
    ISubscriber subscriber = Aggregator.from(dto);

    publisher.subscribe(subscriber);

    // Notificarle TODAS las sources actuales SOLO al nuevo subscriber
    sourceService.findAllSources().forEach(subscriber::notifySource);

    // Notificarle TODOS los eventos actuales SOLO al nuevo subscriber
    subscriber.notifyNewEvents(eventService.getEvents(null));
  }
}
