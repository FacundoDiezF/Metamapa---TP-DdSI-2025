package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.output.EditionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.edition.EditionStatus;
import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.repositories.IEditionRequestRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.services.IEditionRequestService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EditionRequestService implements IEditionRequestService {

  private final IEditionRequestRepository editionRequestRepository;

  private final IEventRepository eventRepository;

  private final Publisher publisher;

  @Autowired
  public EditionRequestService(IEditionRequestRepository editionRequestRepository, IEventRepository eventRepository, Publisher publisher) {
    this.editionRequestRepository = editionRequestRepository;
    this.eventRepository = eventRepository;
    this.publisher = publisher;
  }

  @Override
  public Page<EditionRequestOutputDTO> getEditionRequests(EditionStatus status, Pageable pageable) {
    return editionRequestRepository.findByStatus(status, pageable).map(EditionRequestOutputDTO::from);
  }

  @Override
  @Transactional
  public EditionRequestOutputDTO evaluate(String userId, Long editionRequestId, EditionStatus newEvaluationStatus) {

    EventEditionRequest editionRequest = editionRequestRepository.findById(editionRequestId)
        .orElseThrow(() -> new NotFoundException("Edition-Request not found for ID: " + editionRequestId));

    editionRequest.evaluate(newEvaluationStatus, userId);

    if (editionRequest.getStatus() == EditionStatus.APPROVED) {
      Event editedEvent = editionRequest.getEvent();

      editionRequest.applyChangesToEvent();

      eventRepository.save(editedEvent);

      if (editedEvent.isAccepted()) {
        publisher.notifyUpdatedEvent(editedEvent);
      }
    }

    editionRequestRepository.save(editionRequest);

    return EditionRequestOutputDTO.from(editionRequest);
  }
}
