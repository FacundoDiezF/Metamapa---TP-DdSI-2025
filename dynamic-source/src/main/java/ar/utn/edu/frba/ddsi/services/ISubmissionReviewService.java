package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ISubmissionReviewService {
  SubmissionRequestOutputDTO evaluate(SubmissionEvaluationDTO submissionEvaluation);

  List<SubmissionRequestOutputDTO> getSubmissionRequests(SubmissionState state);
}
