package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class SourceRepository implements ISourceRepository {
  private final Map<Long, Source> sources = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public Source save(Source source) {
    //Internal Id
    if (source.getId() == null) {
      Long id = idGenerator.getAndIncrement();
      source.setId(id);
      sources.put(id, source);
    } else {
      sources.put(source.getId(), source);
    }
    return source;
  }

  @Override
  public List<Source> findAll() {
    return sources.values().stream().toList();
  }

  public Source findById(Long id) {
    return sources.get(id);
  }

  @Override
  public Source findByExternalIds(Long sourceClientId, Long sourceId) {
    return this.findAll().stream()
        .filter(s -> s.getSourceClient().getId().equals(sourceClientId) && s.getInClientId().equals(sourceId))
        .findFirst()
        .orElse(null);
  }
}
