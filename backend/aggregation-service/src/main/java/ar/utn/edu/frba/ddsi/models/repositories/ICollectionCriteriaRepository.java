package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionCriteriaRepository extends JpaRepository<CollectionCriteria, Long> {
}
