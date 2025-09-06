package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import ar.utn.edu.frba.ddsi.services.ISubmissionReviewService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("submissions")
public class SubmissionRequestController {

  private final ISubmissionReviewService submissionReviewService;

  @Autowired
  public SubmissionRequestController(ISubmissionReviewService submissionReviewService) {
    this.submissionReviewService = submissionReviewService;
  }

  @GetMapping
  public List<SubmissionRequestOutputDTO> getSubmissionRequests(@RequestParam(name = "state", required = false) SubmissionState state) {
    return submissionReviewService.getSubmissionRequests(state);
  }

  @PutMapping
  public SubmissionRequestOutputDTO evaluateEvent(@RequestBody SubmissionEvaluationDTO eventDto) {
    return submissionReviewService.evaluate(eventDto);
  }
}
