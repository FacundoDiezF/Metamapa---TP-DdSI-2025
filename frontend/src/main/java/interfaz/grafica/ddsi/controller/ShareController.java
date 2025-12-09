package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestEvaluateDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.dtos.output.SubmissionRequestEvaluationDTO;
import interfaz.grafica.ddsi.dtos.output.collection.CollectionOutputDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.DeletionRequestService;
import interfaz.grafica.ddsi.services.EventService;
import interfaz.grafica.ddsi.services.SubmissionRequestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/share")
public class ShareController {

  private final EventService eventService;
  private final DeletionRequestService deletionRequestService;
  private final SubmissionRequestService submissionRequestService;
  private final CollectionService collectionService;

  public ShareController(EventService eventService, DeletionRequestService deletionRequestService, SubmissionRequestService submissionRequestService, CollectionService collectionService) {
    this.eventService = eventService;
    this.deletionRequestService = deletionRequestService;
    this.submissionRequestService = submissionRequestService;
    this.collectionService = collectionService;
  }

  @PostMapping("/event")
  public String handleShareEvent(@ModelAttribute("newEvent") EventOutputDTO newEvent, Model model) {

    eventService.postNew(newEvent);

    return "redirect:/explore";
  }

  @PostMapping("/deletion-request")
  public String handleShareDeletionRequest(@ModelAttribute("newDeletionRequest") DeletionRequestCreationDTO newDeletionRequest, Model model) {

    String modalType;
    String title;
    String description;

    try {
      deletionRequestService.postNew(newDeletionRequest);
      modalType = "success";
      title = "Solicitud en revisión";
      description = "Gracias por colaborar con la comunidad! Revisaremos tu solicitud y actuaremos de acuerdo con las normas de convivencia para mantener MetaMapa útil, seguro y respetuoso para todas las personas.";

    } catch (BackendException e) {

      switch (e.getErrorInfo().getErrorCode()) {
        case "SPAM_DETECTED":
          modalType = "warning";
          title = "Spam detectado";
          description = "Hemos detectado que el contenido de tu envío podría infringir nuestras políticas de spam. Por favor, revisa tu solicitud antes de enviarla nuevamente.";
          break;

        default:
          modalType = "error";
          title = e.getErrorInfo().getErrorCode();
          description = e.getErrorInfo().getMessage();
          break;
      }
    }

    model.addAttribute("mType", modalType);
    model.addAttribute("mTitle", title);
    model.addAttribute("mDesc", description);

    return "fragments/modals/_messageModals :: messageModal(type=${mType}, title=${mTitle}, desc=${mDesc})";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/deletion-request/evaluate/{dReqId}")
  public String handleEvaluateDeletionRequest(
      @PathVariable Long dReqId,
      @RequestParam("approve") boolean approve,
      Model model
  ) {

    DeletionRequestEvaluateDTO evaluationDto = new DeletionRequestEvaluateDTO();
    evaluationDto.setAccepted(approve);

    deletionRequestService.evaluate(dReqId, evaluationDto);
    return "redirect:/admin/deletionRequests";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/submission-request/evaluate/{sReqId}")
  public String handleEvaluateSubmissionRequest(
      @PathVariable Long sReqId,
      @RequestParam("approve") boolean approve,
      Model model
  ) {

    SubmissionRequestEvaluationDTO evaluationDto = new SubmissionRequestEvaluationDTO(approve, null);
    //TODO: suggestion, deberiamos recibir y pasar al constructor las sugerrencias en caso de que un admin deseeagregar

    submissionRequestService.evaluate(sReqId, evaluationDto);
    return "redirect:/admin/pendingEvents";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/collections")
  public String createCollection(@ModelAttribute CollectionOutputDTO collectionForm) {

    collectionService.postNew(collectionForm);

    return "redirect:/admin/collections";
  }
}
