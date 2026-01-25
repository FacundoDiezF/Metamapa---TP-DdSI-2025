package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("events")
public class EventController {

  private final IEventService eventService;

  @Autowired
  public EventController(IEventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping
  public List<EventOutputDTO> getEvents() {
    return eventService.getAllEvents();
  }

  @PostMapping
  public EventOutputDTO submitEvent(@RequestHeader(value = "X-User-Id", required = false) String userId, @RequestBody EventDTO eventDto) {
    return eventService.save(eventDto, userId);
  }

  @PutMapping("/{eventId}")
  public void updateEvent(@RequestHeader(value = "X-User-Id", required = false) String userId, @PathVariable Long eventId, @RequestBody EventDTO eventDto) {
    eventService.update(userId, eventId, eventDto);
  }

  @DeleteMapping("/{eventId}")
  public EventOutputDTO deleteEvent(@PathVariable Long eventId) {
    return eventService.deleteEvent(eventId);
  }
}
