package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.collection.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICollectionRepository extends JpaRepository<Collection, String> {
}
