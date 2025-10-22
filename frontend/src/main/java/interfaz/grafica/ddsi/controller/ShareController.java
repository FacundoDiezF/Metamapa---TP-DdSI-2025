package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.output.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.DeletionRequestService;
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
  private final DeletionRequestService deletionRequestService;

  public ShareController(EventService eventService, DeletionRequestService deletionRequestService) {
    this.eventService = eventService;
    this.deletionRequestService = deletionRequestService;
  }

  @PostMapping("/event")
  public String handleShareEvent(@ModelAttribute("newEvent") EventOutputDTO newEvent, Model model) {

    eventService.postNew(newEvent);

    return "redirect:/explore";
  }

  @PostMapping("/deletion-request")
  public String handleShareDeletionRequest(@ModelAttribute("newDeletionRequest") DeletionRequestDTO newDeletionRequest, Model model) {

    deletionRequestService.postNew(newDeletionRequest);

    return "fragments/modals/_reportEventModal :: deletionRequestSuccesseModal";
  }


}
