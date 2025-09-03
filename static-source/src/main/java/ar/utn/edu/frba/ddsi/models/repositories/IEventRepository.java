package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {
  List<Event> findByDeleted(boolean deleted);

  //Not deleted & by sourceId
  @Query("""
        SELECT e
        FROM Event e
        WHERE e.deleted = false AND e.source.id = :sourceId
        """)
  List<Event> findBySourceId(Long sourceId);
}
