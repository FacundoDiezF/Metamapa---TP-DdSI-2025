package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {

  private final IEventService eventService;

  public EventController(IEventService eventService) {
    this.eventService = eventService;
  }


  @GetMapping
  public Page<EventOutputDTO> getEvents(
      @RequestParam(required = false) List<Long> category_in,
      @RequestParam(required = false) List<Long> province_in,
      @RequestParam(required = false) List<Origin> source_in,
      @RequestParam(required = false) LocalDateTime uploadDate_lt,
      @RequestParam(required = false) LocalDateTime uploadDate_gt,
      @RequestParam(required = false) LocalDateTime eventDate_lt,
      @RequestParam(required = false) LocalDateTime eventDate_gt,
      @RequestParam(required = false) LocalDateTime fromUpdatedDate, //TODO: Cambiar nomenclatura, pero esto afecta al modulo de estadisticas
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    return eventService.getEvents(category_in, province_in, source_in, uploadDate_lt, uploadDate_gt, eventDate_lt, eventDate_gt, fromUpdatedDate, pageable);
  }

  @PostMapping
  public void createEvents(@RequestBody List<EventInputDTO> dtos) {
    eventService.createAll(dtos);
  }

  @PutMapping
  public void updateEvents(@RequestBody List<EventInputDTO> dtos) {
    eventService.updateAll(dtos);
  }
}
