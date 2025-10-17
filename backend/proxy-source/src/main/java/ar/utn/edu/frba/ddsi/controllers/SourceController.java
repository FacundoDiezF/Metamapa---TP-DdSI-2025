package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sources")
public class SourceController {

  @Autowired
  ISourceService sourceService;

  @PostMapping
  public void create(@RequestBody SourceInputDTO dto) {
    sourceService.create(dto);
  }

  @GetMapping
  public List<SourceOutputDTO> getAllSources() {
    return sourceService.getAllSources();
  }

  @GetMapping("{id}/events")
  public List<EventOutputDTO> getAllEventsBySourceId(@PathVariable Long id) {
    return sourceService.getAllEventsBySourceId(id);
  }
}