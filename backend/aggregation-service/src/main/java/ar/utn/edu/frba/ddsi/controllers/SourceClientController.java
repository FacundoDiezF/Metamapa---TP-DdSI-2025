package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceClientOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.ISourceClientService;
import ar.utn.edu.frba.ddsi.services.impl.EventService;
import ar.utn.edu.frba.ddsi.services.impl.SourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/source-clients")
public class SourceClientController {

  private final ISourceClientService sourceClientService;
  private final EventService eventService;
  private final SourceService sourceService;

  @Autowired
  public SourceClientController(ISourceClientService sourceClientService, EventService eventService, SourceService sourceService) {
    this.sourceClientService = sourceClientService;
    this.eventService = eventService;
    this.sourceService = sourceService;
  }

  @PostMapping
  public SourceClientOutputDTO createSourceClient(@RequestBody SourceClientInputDTO sourceClient) {
    return sourceClientService.create(sourceClient);
  }

  @GetMapping
  public List<SourceClientOutputDTO> getAllClients() {
    return sourceClientService.getAllClients();
  }

  @PostMapping("/{sourceClientId}/sources")
  public SourceOutputDTO createSource(@RequestBody SourceInputDTO sourceDto, @PathVariable Long sourceClientId) {
    sourceDto.setSourceClientId(sourceClientId);
    return sourceService.create(sourceDto);
  }

  @PostMapping("/{sourceClientId}/events")
  public void createEvents(@RequestBody List<EventInputDTO> dtos, @PathVariable Long sourceClientId) {
    eventService.createAll(dtos, sourceClientId);
  }

  @PutMapping("/{sourceClientId}/events")
  public void updateEvents(@RequestBody List<EventInputDTO> dtos, @PathVariable Long sourceClientId) {
    eventService.updateAll(dtos, sourceClientId);
  }
}