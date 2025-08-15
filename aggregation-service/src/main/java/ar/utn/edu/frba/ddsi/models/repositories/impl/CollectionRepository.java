package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class CollectionRepository implements ICollectionRepository {

  private final Map<String, Collection> collections = new HashMap<>();

  @Override
  public void save(Collection collection) {
    if (collection.getHandler() == null) {
      String handler = collection.getTitle().replaceAll(" ", "_"); //TODO: verificar que es realmente el handler (por ahora tomamos el title de la coleccion)
      collection.setHandler(handler);
      collections.put(handler, collection);
    } else {
      collections.put(collection.getHandler(), collection);
    }
  }

  @Override
  public List<Collection> findAll() {
    return collections.values().stream().toList();
  }

  @Override
  public Collection findByHandler(String handler) {
    return collections.get(handler);
  }
}