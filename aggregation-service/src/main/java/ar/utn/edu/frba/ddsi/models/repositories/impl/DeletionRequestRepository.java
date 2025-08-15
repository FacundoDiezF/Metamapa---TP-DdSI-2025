package ar.utn.edu.frba.ddsi.models.repositories.impl;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import ar.utn.edu.frba.ddsi.models.repositories.IDeletionRequestRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class DeletionRequestRepository implements IDeletionRequestRepository {
  private final Map<Long, DeletionRequest> deletionRequests = new HashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public void save(DeletionRequest deletionRequest) {
    if (deletionRequest.getId() == null) {
      Long id = idGenerator.getAndIncrement();
      deletionRequest.setId(id);
      deletionRequests.put(id, deletionRequest);
    } else {
      deletionRequests.put(deletionRequest.getId(), deletionRequest);
    }
  }

  @Override
  public List<DeletionRequest> findAll() {
    return deletionRequests.values().stream().toList();
  }

  @Override
  public DeletionRequest getById(long id) {
    return deletionRequests.get(id);
  }

  @Override
  public List<DeletionRequest> getByEventId(long eventId) {
    return deletionRequests.values().stream().filter(dr -> dr.getEvent().getId() == eventId).collect(Collectors.toList());
  }
}
