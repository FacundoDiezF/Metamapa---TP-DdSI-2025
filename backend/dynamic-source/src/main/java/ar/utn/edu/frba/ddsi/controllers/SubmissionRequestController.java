package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import ar.utn.edu.frba.ddsi.services.ISubmissionRequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("submissions")
public class SubmissionRequestController {

  private final ISubmissionRequestService submissionRequestService;

  @Autowired
  public SubmissionRequestController(ISubmissionRequestService submissionRequestService) {
    this.submissionRequestService = submissionRequestService;
  }

  @GetMapping
  public Page<SubmissionRequestOutputDTO> getSubmissionRequests(
      @RequestParam(name = "state", defaultValue = "PENDING") SubmissionState state,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    return submissionRequestService.getSubmissionRequests(state, pageable);
  }

  @PutMapping("/{submissionId}")
  public SubmissionRequestOutputDTO evaluateEvent(@PathVariable Long submissionId, @RequestBody SubmissionEvaluationDTO evaluationDTO) {
    return submissionRequestService.evaluate(submissionId, evaluationDTO);
  }
}
