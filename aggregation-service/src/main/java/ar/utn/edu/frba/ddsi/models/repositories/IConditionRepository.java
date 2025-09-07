package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IConditionRepository extends JpaRepository<Condition, Long> {

}
