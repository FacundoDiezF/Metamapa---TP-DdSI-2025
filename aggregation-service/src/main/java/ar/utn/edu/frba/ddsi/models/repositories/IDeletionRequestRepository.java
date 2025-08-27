package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import java.util.List;

public interface IDeletionRequestRepository {
  void save(DeletionRequest deletionRequest);

  List<DeletionRequest> findAll();

  DeletionRequest getById(long id);

  List<DeletionRequest> getByEventId(long eventId);
}
