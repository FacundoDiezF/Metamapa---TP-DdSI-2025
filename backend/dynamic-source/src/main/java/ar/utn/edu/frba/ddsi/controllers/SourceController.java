package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.impl.SourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sources")
public class SourceController {

  private final SourceService sourceService;

  public SourceController(SourceService sourceService) {
    this.sourceService = sourceService;
  }

  @GetMapping
  public List<SourceOutputDTO> getSources() {
    return sourceService.getSources();
  }

  @GetMapping("{id}/events")
  public List<EventOutputDTO> getAllEventsBySourceId(@PathVariable Long id) {
    return sourceService.getAllEventsBySourceId(id);
  }

}
