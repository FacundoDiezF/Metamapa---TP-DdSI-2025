package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request.DeletionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequestState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDeletionRequestService {
  DeletionRequestOutputDTO evaluate(DeletionRequestEvaluationDTO deletionRequestEvaluationDTO);

  DeletionRequestOutputDTO create(DeletionRequestCreationDTO deletionRequestCreationDTO);

  Page<DeletionRequestOutputDTO> getAll(DeletionRequestState state, Pageable pageable);
}
