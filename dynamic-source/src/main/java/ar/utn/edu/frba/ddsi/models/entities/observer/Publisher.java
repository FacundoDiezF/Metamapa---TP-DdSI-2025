package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
  private final Set<ISubscriber> subscribers;

  public Publisher() {
    this.subscribers = new HashSet<>();
  }

  public void subscribe(ISubscriber subscriber) {
    subscribers.add(subscriber);
  }

  public void notifyNewEvent(Event event) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyNewEvent(List.of(EventOutputDTO.from(event)));
    }
  }

  public void notifyUpdatedEvent(Event event) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyUpdatedEvent(EventOutputDTO.from(event));
    }
  }
}
