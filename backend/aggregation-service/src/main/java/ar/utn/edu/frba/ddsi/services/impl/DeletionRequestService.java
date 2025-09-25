package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.exceptions.SpamException;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request.DeletionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import ar.utn.edu.frba.ddsi.models.entities.spamDetector.ISpamDetector;
import ar.utn.edu.frba.ddsi.models.repositories.IDeletionRequestRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.services.IDeletionRequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeletionRequestService implements IDeletionRequestService {

  private final IDeletionRequestRepository deletionRequestRepository;

  private final IEventRepository eventRepository;

  private final ISpamDetector spamDetector;

  @Autowired
  public DeletionRequestService(IDeletionRequestRepository deletionRequestRepository, IEventRepository eventRepository, ISpamDetector spamDetector) {
    this.deletionRequestRepository = deletionRequestRepository;
    this.eventRepository = eventRepository;
    this.spamDetector = spamDetector;
  }


  @Override
  public DeletionRequestOutputDTO create(DeletionRequestCreationDTO drDTO) {

    Event drEvent = eventRepository.findById(drDTO.getEventId())
        .orElseThrow(() -> new NotFoundException("Event not found - ID: " + drDTO.getEventId()));

    DeletionRequest deletionRequest = DeletionRequest.from(drDTO, drEvent);

    // Get a List of the DRs for the same event.
    List<DeletionRequest> eventDeletionRequests = deletionRequestRepository.findByEvent_Id(drDTO.getEventId());

    // Check spam.
    if (spamDetector.isSpam(eventDeletionRequests, deletionRequest.getArgument()) && !eventDeletionRequests.isEmpty()) {
      throw new SpamException("Error: deletion request denied due to spam.");
    }

    deletionRequestRepository.save(deletionRequest);

    return DeletionRequestOutputDTO.from(deletionRequest);
  }

  @Override
  public DeletionRequestOutputDTO evaluate(DeletionRequestEvaluationDTO evaluation) {

    //TODO: validate that user can do this petition.

    DeletionRequest deletionRequest = deletionRequestRepository.findById(evaluation.getDeletionRequestId())
        .orElseThrow(() -> new NotFoundException("Deletion request not found - ID: " + evaluation.getDeletionRequestId()));

    deletionRequest.evaluate(evaluation.isAccepted(), evaluation.getEvaluatorName());

    deletionRequestRepository.save(deletionRequest);
    return DeletionRequestOutputDTO.from(deletionRequest);
  }

  @Override
  public List<DeletionRequestOutputDTO> getAll() {
    return deletionRequestRepository.findAll().stream()
        .map(DeletionRequestOutputDTO::from)
        .toList();
  }
}
