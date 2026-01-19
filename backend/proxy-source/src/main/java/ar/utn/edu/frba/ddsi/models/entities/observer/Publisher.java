package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

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

  public void notifyNewEvents(List<EventOutputDTO> eventsToNotify) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyNewEvents(eventsToNotify).subscribe(null,
          error -> {
            System.err.println("Error notifying events to subscriber: " + error.getMessage());
          });
    }
  }

  public void notifyUpdatedEvents(List<EventOutputDTO> eventToNotify) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyUpdatedEvents(eventToNotify).subscribe(null,
          error -> {
            System.err.println("Error notifying updated event to subscriber: " + error.getMessage());
          });
    }
  }

  public void notifyNewSource(Source source) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifySource(source).subscribe(null,
          error -> {
            System.err.println("Error notifying new source to subscriber: " + error.getMessage());
          });
    }
  }
}
