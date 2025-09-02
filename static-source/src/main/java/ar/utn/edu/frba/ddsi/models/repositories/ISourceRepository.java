package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISourceRepository extends JpaRepository<Source, Long> {
}
