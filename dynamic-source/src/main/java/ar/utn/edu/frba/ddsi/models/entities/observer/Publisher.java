package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class Publisher {
  Set<ISubscriber> subscribers;

  public Publisher() {
    this.subscribers = new HashSet<>();
  }

  public void subscribe(ISubscriber subscriber) {
    subscribers.add(subscriber);
  }

  public void unsubscribe(ISubscriber subscriber) {
    subscribers.remove(subscriber);
  }

  public void notifyNewEvent(Event event) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyNewEvent(List.of(event));
    }
  }

  public void notifyUpdatedEvents(Event event) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyUpdatedEvent(event);
    }
  }
}
