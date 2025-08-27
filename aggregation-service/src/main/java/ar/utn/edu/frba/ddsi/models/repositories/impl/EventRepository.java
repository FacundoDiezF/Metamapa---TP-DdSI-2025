package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository implements IEventRepository {
  private final Map<Long, Event> events = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public void save(Event event) {
    //Internal Id
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
    return events.values().stream().filter(e -> e.isDeleted() == deleted).toList();
  }

  @Override
  public Event findById(Long id) {
    return events.get(id);
  }

  @Override
  public List<Event> findFiltered(
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  ) {
    return filterEvents(
        this.findByDeleted(false),
        category, untilUploadDate, fromUploadDate, untilEventDate, fromEventDate
    );
  }

  @Override
  public Event findByExternalIds(Long sourceClientId, Long sourceId, Long inSourceEventId) {
    return events.values().stream()
        .filter(e -> e.getSource().getSourceClient().getId().equals(sourceClientId) &&
            e.getSource().getInClientId().equals(sourceId) &&
            e.getInSourceEventId().equals(inSourceEventId))
        .findFirst()
        .orElse(null);
  }

  private List<Event> filterEvents(
      List<Event> events,
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  ) {
    return events.stream()
        .filter(e -> (category == null || e.getCategory().getName().equals(category)))
        .filter(e -> (untilUploadDate == null || e.getUploadDate().isBefore(untilUploadDate)))
        .filter(e -> (fromUploadDate == null || e.getUploadDate().isAfter(fromUploadDate)))
        .filter(e -> (untilEventDate == null || e.getEventDate().isBefore(untilEventDate)))
        .filter(e -> (fromEventDate == null || e.getEventDate().isAfter(fromEventDate)))
        .toList();
  }
}
