package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

  List<Event> findByDeleted(boolean deleted);

  @Query("""
            SELECT e FROM Event e
            WHERE e.deleted = false
              AND (:category_in IS NULL OR e.category.id IN :category_in)
              AND (:source_in IS NULL OR e.source.type IN :source_in)
              AND (:province_in IS NULL OR e.location.province.id IN :province_in)
              AND (:uploadDate_lt IS NULL OR e.uploadDate < :uploadDate_lt)
              AND (:uploadDate_gt IS NULL OR e.uploadDate > :uploadDate_gt)
              AND (:eventDate_lt IS NULL OR e.eventDate < :eventDate_lt)
              AND (:eventDate_gt IS NULL OR e.eventDate > :eventDate_gt)
              AND (:fromUpdatedDate IS NULL OR e.updatedDate > :fromUpdatedDate)
      """)
  Page<Event> findFiltered(
      List<Long> category_in,
      List<Long> province_in,
      List<Origin> source_in,
      LocalDateTime uploadDate_lt,
      LocalDateTime uploadDate_gt,
      LocalDateTime eventDate_lt,
      LocalDateTime eventDate_gt,
      LocalDateTime fromUpdatedDate,
      Pageable pageable
  );

  @Query("""
      SELECT e FROM Event e
      WHERE e.source.sourceClient.id = :sourceClientId
        AND e.source.originSourceId = :originSourceId
        AND e.originEventId = :originEventId
      """)
  Optional<Event> findByExternalIds(Long sourceClientId, Long originSourceId, Long originEventId);
}
