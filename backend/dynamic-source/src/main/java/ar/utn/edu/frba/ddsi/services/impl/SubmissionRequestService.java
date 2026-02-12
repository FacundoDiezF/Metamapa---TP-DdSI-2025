package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.InvalidStateChangeException;
import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.SubmissionEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SubmissionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.strategies.UrlGenerationStrategy;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.ISubmissionRequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubmissionRequestService implements ISubmissionRequestService {

  private final ISubmissionRepository submissionRepository;

  private final IEventRepository eventRepository;

  private final Publisher publisher;

  private final UrlGenerationStrategy urlGenerationStrategy;

  @Autowired
  private SubmissionRequestService(ISubmissionRepository submissionRepository,
                                   IEventRepository eventRepository,
                                   Publisher publisher,
                                   UrlGenerationStrategy urlGenerationStrategy) {
    this.submissionRepository = submissionRepository;
    this.eventRepository = eventRepository;
    this.publisher = publisher;
    this.urlGenerationStrategy = urlGenerationStrategy;
  }


  @Override
  public SubmissionRequestOutputDTO evaluate(String userId, Long submissionId, SubmissionEvaluationDTO submissionEvaluation) {

    //TODO: Validar si el usuario puede realizar esta peticion

    SubmissionRequest submission = submissionRepository.findById(submissionId)
        .orElseThrow(() -> new NotFoundException("Submission not found - ID: " + submissionId));

    if (!submission.isPending()) {
      throw new InvalidStateChangeException("The event has already been evaluated: " + submission.getState());
    }

    // Updates submission (and the event, if the submission is accepted)
    submission.setEvaluation(submissionEvaluation, userId);

    SubmissionRequest submissionRequest = submissionRepository.save(submission);
    Event eventSaved = eventRepository.save(submission.getEvent());

    if (eventSaved.isAccepted()) {
      publisher.notifyNewEvent(eventSaved);
    }
    return SubmissionRequestOutputDTO.from(submissionRequest);
  }

  @Override
  public Page<SubmissionRequestOutputDTO> getSubmissionRequests(SubmissionState state, Pageable pageable) {
    return submissionRepository.findByState(state, pageable).map(sub -> SubmissionRequestOutputDTO.from(sub, urlGenerationStrategy));
  }

}
