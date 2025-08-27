package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;

public interface ISourceRepository {
  Source save(Source source);
  List<Source> findAll();
  Source findById(Long id);
  Source findByExternalIds(Long sourceClientId, Long sourceId);
}
