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
  public EditionRequestOutputDTO evaluate(Long editionRequestId, EditionStatus newEvaluationStatus) {

    EventEditionRequest editionRequest = editionRequestRepository.findById(editionRequestId)
        .orElseThrow(() -> new NotFoundException("Edition-Request not found for ID: " + editionRequestId));

    editionRequest.setStatus(newEvaluationStatus);
    editionRequest.setEvaluationDate(LocalDateTime.now());
    editionRequest.setEvaluatorId("REMPLAZAR_CON_ID-USUARIO"); // TODO: Reemplazar con el id del usuario admin actual (lo toma del header de la peticion)
    editionRequestRepository.save(editionRequest);

    // Si fue aprobada se actualizan los datos del evento y se notifica a los observadores
    if (editionRequest.getStatus() == EditionStatus.APPROVED) {
      Event editedEvent = editionRequest.getEvent();

      editedEvent.setUpdateDate(LocalDateTime.now());

      if (!Objects.equals(editionRequest.getNewTitle(), editedEvent.getTitle())) {
        editedEvent.setTitle(editionRequest.getNewTitle());
      }
      if (!Objects.equals(editionRequest.getNewDescription(), editedEvent.getDescription())) {
        editedEvent.setDescription(editionRequest.getNewDescription());
      }
      if (editionRequest.getNewCategory() != null && !Objects.equals(editionRequest.getNewCategory(), editedEvent.getCategory())) {
        editedEvent.setCategory(editionRequest.getNewCategory());
      }
      if (editionRequest.getNewLatitude() != null && !Objects.equals(editionRequest.getNewLatitude(), editedEvent.getLatitude())) {
        editedEvent.setLatitude(editionRequest.getNewLatitude());
      }
      if (editionRequest.getNewLongitude() != null && !Objects.equals(editionRequest.getNewLongitude(), editedEvent.getLongitude())) {
        editedEvent.setLongitude(editionRequest.getNewLongitude());
      }
      if (editionRequest.getNewEventDate() != null && !Objects.equals(editionRequest.getNewEventDate(), editedEvent.getEventDate())) {
        editedEvent.setEventDate(editionRequest.getNewEventDate());
      }

      eventRepository.save(editedEvent);

      if (editedEvent.isAccepted()) {
        publisher.notifyUpdatedEvent(editedEvent);
      }
    }

    return EditionRequestOutputDTO.from(editionRequest);
  }
}
