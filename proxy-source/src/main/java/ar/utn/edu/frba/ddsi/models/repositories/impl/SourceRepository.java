package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class SourceRepository implements ISourceRepository {

  Map<Long, Source> sources = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public void save(Source source) {
    if (source.getId() == null) {
      Long id = idGenerator.getAndIncrement();
      source.setId(id);
      sources.put(id, source);
    } else {
      sources.put(source.getId(), source);
    }
  }

  @Override
  public List<Source> findAll() {
    return new ArrayList<>(sources.values());
  }

  @Override
  public List<Source> findNonMetaMapa() {
    return sources.values().stream()
        .filter(source -> source.getType() != Origin.METAMAPA)
        .toList();
  }

  @Override
  public Source findById(Long sourceId) {
    return sources.get(sourceId);
  }
}