package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

  List<Event> findByDeleted(boolean deleted);


  //Not eliminated & Filtered
  List<Event> findFiltered( //TODO Implementar
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  );

  //@Query("SELECT e FROM Event e WHERE e.source.sourceClient.id = :sourceClientId AND e.source.originSourceId = :sourceId AND e.originEventId = :inSourceEventId")
  // TODO Implementar
  Event findByExternalIds(Long sourceClientId, Long sourceId, Long inSourceEventId);
}
