package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;

public interface ISourceRepository {
  void save(Source source);

  List<Source> findAll();

  List<Source> findNonMetaMapa();

  Source findById(Long id);
}
