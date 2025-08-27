package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
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

  public void notifyNewEvents(List<EventOutputDTO> events) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifyNewEvents(events);
    }
  }

  public void notifyNewSource(SourceOutputDTO source) {
    for (ISubscriber subscriber : subscribers) {
      subscriber.notifySource(source);
    }
  }
}
