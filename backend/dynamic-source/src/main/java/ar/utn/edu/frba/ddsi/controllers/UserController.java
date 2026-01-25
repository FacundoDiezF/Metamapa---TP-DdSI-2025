package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

  private final IEventService eventService;

  @Autowired
  public UserController(IEventService eventService) {
    this.eventService = eventService;
  }

  @GetMapping("/me/events")
  public List<EventOutputDTO> getUserEvents(@RequestHeader(value = "X-User-Id", required = true) String userId) {
    return eventService.getEventsByUserId(userId);
  }
}
