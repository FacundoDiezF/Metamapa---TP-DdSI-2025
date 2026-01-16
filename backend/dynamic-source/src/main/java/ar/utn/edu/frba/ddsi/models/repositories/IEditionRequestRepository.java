package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.edition.EditionStatus;
import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEditionRequestRepository extends JpaRepository<EventEditionRequest, Long> {

  Page<EventEditionRequest> findByStatus(EditionStatus state, Pageable pageable);

}
