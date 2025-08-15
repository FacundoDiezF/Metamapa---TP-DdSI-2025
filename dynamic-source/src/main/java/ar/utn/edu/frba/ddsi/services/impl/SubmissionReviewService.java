package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.InvalidStateChangeException;
import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionEvaluationOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.ISubmissionReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionReviewService implements ISubmissionReviewService {

  @Autowired
  ISubmissionRepository submissionRepository;

  @Autowired
  IEventRepository eventRepository;

  @Autowired
  Publisher publisher;


  @Override
  public SubmissionEvaluationOutputDTO evaluate(SubmissionEvaluationDTO submissionEvaluation) {

    //TODO: Validar si el usuario puede realizar esta peticion

    SubmissionRequest submission = submissionRepository.findById(submissionEvaluation.getSubmissionId());
    if (submission == null)
      throw new NotFoundException("Submission not found - ID: " + submissionEvaluation.getSubmissionId());

    if (!submission.isPending()) {
      throw new InvalidStateChangeException("The event has already been evaluated: " + submission.getState());
    }

    // Updates submission (and the event, if the submission is accepted)
    submission.setEvaluation(submissionEvaluation);

    SubmissionRequest submissionRequest = submissionRepository.save(submission);
    Event eventSaved = eventRepository.save(submission.getEvent());

    if (eventSaved.isAccepted()) {
      publisher.notifyNewEvent(eventSaved);
    }
    return SubmissionEvaluationOutputDTO.from(eventSaved, submissionRequest.getState());
  }

}
