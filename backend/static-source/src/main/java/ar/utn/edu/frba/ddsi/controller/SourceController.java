package ar.utn.edu.frba.ddsi.controller;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.services.ISourceService;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/sources")
public class SourceController {

  private final ISourceService sourceService;

  public SourceController(ISourceService sourceService) {
    this.sourceService = sourceService;
  }


  @PostMapping
  public SourceOutputDTO createSource(@RequestParam("csvfile") MultipartFile csvfile) throws IOException {
    System.out.println("[BACK] Llego CSV: nombre=" + csvfile.getOriginalFilename() + ", size=" + csvfile.getSize());

    SourceOutputDTO dto = sourceService.createFromCsvFile(csvfile);

    System.out.println("[BACK] Source creada");

    return dto;
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
