package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request.DeletionRequestOutputDTO;
import java.util.List;

public interface IDeletionRequestService {
  DeletionRequestOutputDTO evaluate(DeletionRequestEvaluationDTO deletionRequestEvaluationDTO);

  DeletionRequestOutputDTO create(DeletionRequestCreationDTO deletionRequestCreationDTO);

  List<DeletionRequestOutputDTO> getAll();
}
