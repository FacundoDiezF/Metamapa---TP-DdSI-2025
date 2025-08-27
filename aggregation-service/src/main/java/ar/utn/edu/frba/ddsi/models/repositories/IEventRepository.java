package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository {
  void save(Event event);

  List<Event> findByDeleted(boolean deleted);

  Event findById(Long eventId);

  //Not eliminated & Filtered
  List<Event> findFiltered(
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  );

  Event findByExternalIds(Long sourceClientId, Long sourceId, Long inSourceEventId);
}
