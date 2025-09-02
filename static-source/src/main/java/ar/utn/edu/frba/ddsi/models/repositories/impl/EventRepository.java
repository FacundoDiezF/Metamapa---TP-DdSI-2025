package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository /*implements IEventRepository*/ {
  /*Map<Long, Event> events = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public void save(Event event) {
    if (event.getId() == null) {
      Long id = idGenerator.getAndIncrement();
      event.setId(id);
      events.put(id, event);
    } else {
      events.put(event.getId(), event);
    }
  }

  @Override
  public List<Event> findByDeleted(boolean deleted) {
    return events
        .values()
        .stream()
        .filter(e -> e.isDeleted() == deleted).toList();
  }

  public Event findById(Long eventId) {
    return events.get(eventId);
  }

  @Override
  public List<Event> findBySourceId(Long sourceId) {

    List<Event> eventsBySource = this.findByDeleted(false);

    return eventsBySource
        .stream()
        .filter(event -> event.getSourceId().equals(sourceId)) //!
        .toList();
  }
*/
}
