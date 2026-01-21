package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.EditionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.edition.EditionStatus;
import ar.utn.edu.frba.ddsi.services.IEditionRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("edition-requests")
public class EventEditionRequestController {

  private final IEditionRequestService editionRequestService;

  @Autowired
  public EventEditionRequestController(IEditionRequestService editionRequestService) {
    this.editionRequestService = editionRequestService;
  }

  @GetMapping
  public Page<EditionRequestOutputDTO> getEditionRequests(
      @RequestParam(name = "status", defaultValue = "PENDING") EditionStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    return editionRequestService.getEditionRequests(status, pageable);
  }

  @PutMapping("/{editionRequestId}")
  public EditionRequestOutputDTO evaluateEvent(
      @RequestHeader(value = "X-User-Id") String userId,
      @PathVariable Long editionRequestId,
      @RequestParam EditionStatus newEvaluationStatus
  ) {
    return editionRequestService.evaluate(userId, editionRequestId, newEvaluationStatus);
  }
}
