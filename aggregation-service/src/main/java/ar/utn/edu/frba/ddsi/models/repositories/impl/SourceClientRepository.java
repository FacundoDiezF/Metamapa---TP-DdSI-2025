package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.source.impl.SourceClient;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceClientRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class SourceClientRepository implements ISourceClientRepository {

  private final Map<Long, SourceClient> sourceClients = new HashMap<>(); //<idSourceClient,sourceClient>
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public SourceClient save(SourceClient sourceClient) {
    if (sourceClient.getId() == null) {
      Long id = idGenerator.getAndIncrement();
      sourceClient.setId(id);
      sourceClients.put(id, sourceClient);
    } else {
      sourceClients.put(sourceClient.getId(), sourceClient);
    }
    return sourceClient;
  }

  @Override
  public SourceClient getById(Long id) {
    return sourceClients.get(id);
  }

  @Override
  public List<SourceClient> getAllClients() {
    return sourceClients.values().stream().toList();
  }
}
