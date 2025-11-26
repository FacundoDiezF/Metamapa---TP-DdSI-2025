package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.output.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
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

    try {
      deletionRequestService.postNew(newDeletionRequest);
    } catch (BackendException e) {

      if ("SPAM_DETECTED".equals(e.getErrorInfo().getErrorCode())) {
        model.addAttribute("warningTitle", "Spam detectado");
        model.addAttribute("warningDescription", "Hemos detectado que el contenido de tu envío podría infringir nuestras políticas de spam.");
        return "fragments/modals/_messageModals :: warningModal(title=${warningTitle}, desc=${warningDescription})";
      }

      model.addAttribute("errorTitle", e.getErrorInfo().getErrorCode());
      model.addAttribute("errorDescription", e.getErrorInfo().getMessage());
      return "fragments/modals/_messageModals :: errorModal(title=${errorTitle}, desc=${errorDescription})";
    }

    model.addAttribute("succesTitle", "Solicitud en revisión");
    model.addAttribute("succesDescriptioni",
        "Gracias por colaborar con la comunidad! Revisaremos tu solicitud y actuaremos de acuerdo con las normas de convivencia para mantener MetaMapa útil, seguro y respetuoso para todas las personas."
    );
    return "fragments/modals/_messageModals :: successeModal(title=${succesTitle}, desc=${succesDescriptioni})";
  }
}
