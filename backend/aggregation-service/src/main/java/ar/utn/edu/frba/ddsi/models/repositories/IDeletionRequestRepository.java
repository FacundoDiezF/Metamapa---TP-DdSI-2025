package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequestState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDeletionRequestRepository extends JpaRepository<DeletionRequest, Long> {

  List<DeletionRequest> findByEvent_Id(long eventId);

  Page<DeletionRequest> findAllByState(DeletionRequestState state, Pageable pageable);
}
