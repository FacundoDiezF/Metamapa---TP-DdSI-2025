package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionRepository extends JpaRepository<Collection, Long> {
  Collection findByHandler(String handler);

  List<Collection> findAllByUpdatedDateAfter(LocalDateTime fromUpdateDate);
}