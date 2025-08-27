package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISourceRepository extends JpaRepository<Source, Long> {
  //TODO Probar
  @Query("""
    SELECT s FROM Source s
    WHERE s.sourceClient.id = :sourceClientId
      AND s.originSourceId = :sourceId
    """)
  Source findByExternalIds(Long sourceClientId, Long sourceId);
}
