package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request.DeletionRequestEvaluationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.deletion_request.DeletionRequestOutputDTO;
import ar.utn.edu.frba.ddsi.services.IDeletionRequestService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deletion-request")
public class DeletionRequestController {

  private final IDeletionRequestService deletionRequestService;

  public DeletionRequestController(IDeletionRequestService deletionRequestService) {
    this.deletionRequestService = deletionRequestService;
  }

  @PostMapping
  public DeletionRequestOutputDTO create(@RequestBody DeletionRequestCreationDTO dto) {
    return deletionRequestService.create(dto);
  }

@PutMapping("/{id}")
public DeletionRequestOutputDTO evaluate(@PathVariable Long id, @RequestBody DeletionRequestEvaluationDTO dto) {
  dto.setDeletionRequestId(id);
  return deletionRequestService.evaluate(dto);
}

  @GetMapping
  public List<DeletionRequestOutputDTO> get() {
    return deletionRequestService.getAll();
  }
}