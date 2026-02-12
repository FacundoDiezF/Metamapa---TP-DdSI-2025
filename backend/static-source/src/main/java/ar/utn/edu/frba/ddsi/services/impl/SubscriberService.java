package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.observer.Aggregator;
import ar.utn.edu.frba.ddsi.models.entities.observer.ISubscriber;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import ar.utn.edu.frba.ddsi.services.ISubscriberService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SubscriberService implements ISubscriberService {

  Publisher publisher;
  IEventService eventService;
  ISourceService sourceService;

  public SubscriberService(Publisher publisher, IEventService eventService, ISourceService sourceService) {
    this.publisher = publisher;
    this.eventService = eventService;
    this.sourceService = sourceService;
  }

  @Override
  public void subscribe(String callbackUrl) {
    ISubscriber subscriber = Aggregator.from(callbackUrl);

    publisher.subscribe(subscriber);

    // --- ASYNC SOLUTION ---

    Flux.fromIterable(sourceService.getSources())
        // 1. Send each source in parallel (or fast sequentially)
        .flatMap(source -> subscriber.notifySource(source))

        //2. Wait for all sources to be sent successfully (onComplete), then send the events.
        .then(subscriber.notifyNewEvents(eventService.getEvents()))

        .subscribe(null,

            //Error durante la sincronización inicial:
            error -> System.err.println("Error during initial sync (Sources + Events)" + error.getMessage()),

            () -> System.out.println("Subscriber sync complete (Source + Events) for: " + callbackUrl));
  }

}
