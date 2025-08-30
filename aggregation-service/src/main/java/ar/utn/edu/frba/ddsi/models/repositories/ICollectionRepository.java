package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICollectionRepository extends JpaRepository<Collection, Long> {
  Collection findByHandler(String handler);
}