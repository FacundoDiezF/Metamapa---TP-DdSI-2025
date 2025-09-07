package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.InvalidStateChangeException;
import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.ISubmissionRequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmissionRequestService implements ISubmissionRequestService {

  private final ISubmissionRepository submissionRepository;

  private final IEventRepository eventRepository;

  private final Publisher publisher;

  @Autowired
  private SubmissionRequestService(ISubmissionRepository submissionRepository, IEventRepository eventRepository, Publisher publisher){
    this.submissionRepository = submissionRepository;
    this.eventRepository = eventRepository;
    this.publisher = publisher;
  }


  @Override
  public SubmissionRequestOutputDTO evaluate(Long submissionId, SubmissionEvaluationDTO submissionEvaluation) {

    //TODO: Validar si el usuario puede realizar esta peticion

    SubmissionRequest submission = submissionRepository.findById(submissionId)
        .orElseThrow(() -> new NotFoundException("Submission not found - ID: " + submissionId));

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
    return SubmissionRequestOutputDTO.from(submissionRequest);
  }

  @Override
  public List<SubmissionRequestOutputDTO> getSubmissionRequests(SubmissionState state) {

    if(state == null) {
      return submissionRepository.findAll().stream().map(SubmissionRequestOutputDTO::from).toList();
    }

    return submissionRepository.findByState(state).stream().map(SubmissionRequestOutputDTO::from).toList();
  }

}
