package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import java.util.List;

public interface ICollectionRepository {
  void save(Collection collection);
  void delete(String handler);

  List<Collection> findAll();

  Collection findByHandler(String handler);
}