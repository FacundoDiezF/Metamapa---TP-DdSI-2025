package ar.utn.edu.frba.ddsi.controller;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.impl.SourceService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sources")
public class SourceController {

  private final SourceService sourceService;

  public SourceController(SourceService sourceService) {
    this.sourceService = sourceService;
  }

  @PostMapping
  public SourceOutputDTO createSource(@RequestBody SourceInputDTO dto) {
    return sourceService.create(dto);
  }

  @GetMapping
  public List<SourceOutputDTO> getSources() {
    return sourceService.getSources();
  }

  @PostMapping("{id}/import")
  public void importSourceEvents(@PathVariable Long id) {
    sourceService.importSourceEvents(id);
  }

  @GetMapping("{id}/events")
  public List<EventOutputDTO> getAllEventsBySourceId(@PathVariable Long id) {
    return sourceService.getAllEventsBySourceId(id);
  }

}
