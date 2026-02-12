package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.EditionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.edition.EditionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEditionRequestService {

  Page<EditionRequestOutputDTO> getEditionRequests(EditionStatus status, Pageable pageable);

  EditionRequestOutputDTO evaluate(String userId, Long editionRequestId, EditionStatus newEvaluationStatus);
}
