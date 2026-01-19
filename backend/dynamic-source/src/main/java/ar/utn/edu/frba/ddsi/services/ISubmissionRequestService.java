package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ISubmissionRequestService {
  SubmissionRequestOutputDTO evaluate(String userId, Long submissionId, SubmissionEvaluationDTO submissionEvaluation);

  Page<SubmissionRequestOutputDTO> getSubmissionRequests(SubmissionState state, Pageable pageable);
}
