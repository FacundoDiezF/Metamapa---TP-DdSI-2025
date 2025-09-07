package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEventRepository extends JpaRepository<Event, Long> {

  //Not eliminated & accepted
  @Query("SELECT e FROM Event e WHERE e.deleted = false AND e.accepted = true")
  List<Event> findByAccepted();

  List<Event> findBySourceId(Long sourceId);
}
