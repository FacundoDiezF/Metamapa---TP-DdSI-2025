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

  //TODO Probar
  @Query("""
    SELECT e FROM Event e
    WHERE e.deleted = false
      AND (:category IS NULL OR e.category.name = :category)
      AND (:untilUploadDate IS NULL OR e.uploadDate < :untilUploadDate)
      AND (:fromUploadDate IS NULL OR e.uploadDate > :fromUploadDate)
      AND (:untilEventDate IS NULL OR e.eventDate < :untilEventDate)
      AND (:fromEventDate IS NULL OR e.eventDate > :fromEventDate)
    """)
  List<Event> findFiltered(
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate
  );

  //TODO Probar
  @Query("""
    SELECT e FROM Event e
    WHERE e.source.sourceClient.id = :sourceClientId
      AND e.source.originSourceId = :originSourceId
      AND e.originEventId = :originEventId
    """)
  Event findByExternalIds(Long sourceClientId, Long originSourceId, Long originEventId);

  //Versión mágica (está piola que sea asi de sencillo, pero no pinta usar un method con un nombre tan largo)
  Event findBySource_SourceClient_IdAndSource_OriginSourceIdAndOriginEventId(
      Long sourceClientId,
      Long originSourceId,
      Long originEventId
  );
}
