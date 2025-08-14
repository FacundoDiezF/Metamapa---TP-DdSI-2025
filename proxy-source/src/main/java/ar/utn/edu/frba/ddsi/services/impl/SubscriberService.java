package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.observer.Aggregator;
import ar.utn.edu.frba.ddsi.models.entities.observer.ISubscriber;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import ar.utn.edu.frba.ddsi.services.ISubscriberService;
import java.util.List;
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

    List<Source> sources = sourceService.findAllSources();

    // Notificarle TODAS las sources actuales SOLO al nuevo subscriber
    sources.forEach(subscriber::notifySource);

    sources.stream().filter(source -> source.getType() != Origin.METAMAPA) //? Migrar a service
        .forEach(source -> {
          // Notificarle TODOS los eventos actuales de cada source SOLO al nuevo subscriber
          subscriber.notifyEvents(source.importEvents(null));
        });
  }
}
