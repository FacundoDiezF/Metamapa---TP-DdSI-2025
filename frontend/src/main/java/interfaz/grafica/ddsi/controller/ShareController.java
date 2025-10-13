package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/share")
public class ShareController {

  private final EventService eventService;

  public ShareController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/event")
  public String handleShareForm(@ModelAttribute("newEvent") EventOutputDTO newEvent, Model model) {

    eventService.postNew(newEvent);

    return "redirect:/explore#shareSuccess";
  }


}
