package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceClientOutputDTO;
import ar.utn.edu.frba.ddsi.services.ISourceClientService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/source-clients")
public class SourceClientController {

  private final ISourceClientService sourceClientService;

  public SourceClientController(ISourceClientService sourceClientService) {
    this.sourceClientService = sourceClientService;
  }

  @PostMapping
  public SourceClientOutputDTO createSource(@RequestBody SourceClientInputDTO sourceClient) {
    return sourceClientService.create(sourceClient);
  }

  @GetMapping
  public List<SourceClientOutputDTO> getAllClients() {
    return sourceClientService.getAllClients();
  }
}