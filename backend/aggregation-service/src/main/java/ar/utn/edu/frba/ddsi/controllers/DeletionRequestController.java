package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request.DeletionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequestState;
import ar.utn.edu.frba.ddsi.services.IDeletionRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deletion-request")
public class DeletionRequestController {

  private final IDeletionRequestService deletionRequestService;

  public DeletionRequestController(IDeletionRequestService deletionRequestService) {
    this.deletionRequestService = deletionRequestService;
  }

  @PostMapping
  public DeletionRequestOutputDTO create(@RequestHeader(value = "X-User-Id", required = false) String userId, @RequestBody DeletionRequestCreationDTO dto) {
    return deletionRequestService.create(dto, userId);
  }

  @PutMapping("/{id}")
  public DeletionRequestOutputDTO evaluate(@PathVariable Long id, @RequestBody DeletionRequestEvaluationDTO dto) {
    dto.setDeletionRequestId(id);
    return deletionRequestService.evaluate(dto);
  }

  @GetMapping
  public Page<DeletionRequestOutputDTO> get(
      @RequestParam(defaultValue = "PENDING") DeletionRequestState state,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    return deletionRequestService.getAll(state, pageable);
  }
}