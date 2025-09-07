package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICollectionCriteriaRepository extends JpaRepository<CollectionCriteria, Long> {
}
