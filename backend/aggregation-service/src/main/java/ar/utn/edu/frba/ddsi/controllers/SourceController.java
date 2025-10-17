package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: Deberiamos tener un controlador y un service para los clientes, y otro para las fuentes en si.

@RestController
@RequestMapping("/sources")
public class SourceController {

  private final ISourceService sourceService;

  @Autowired
  public SourceController(ISourceService sourceService) {
    this.sourceService = sourceService;
  }

  @PostMapping
  public SourceOutputDTO createSource(@RequestBody SourceInputDTO source) {
    //TODO enviar todas las sources en una sola request
    return sourceService.create(source);
  }

  @GetMapping
  public List<SourceOutputDTO> getAllSources() {
    return sourceService.getAllSources();
  }
}
