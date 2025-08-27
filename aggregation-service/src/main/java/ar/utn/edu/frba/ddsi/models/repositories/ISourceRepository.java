package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISourceRepository extends JpaRepository<Source, Long> {
  Source findByExternalIds(Long sourceClientId, Long sourceId);//TODO Implementar
}
