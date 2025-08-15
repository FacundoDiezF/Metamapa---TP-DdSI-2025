package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
  public List<EventOutputDTO> getEvents(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) LocalDateTime untilUploadDate,
      @RequestParam(required = false) LocalDateTime fromUploadDate,
      @RequestParam(required = false) LocalDateTime untilEventDate,
      @RequestParam(required = false) LocalDateTime fromEventDate
  ) {
    return eventService.getEvents(category, untilUploadDate, fromUploadDate, untilEventDate, fromEventDate);
  }

  @PostMapping
  public void createEvents(@RequestBody List<EventInputDTO> dtos) {
    eventService.createAll(dtos);
  }
}
