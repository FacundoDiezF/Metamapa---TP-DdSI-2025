package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.EventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private final CollectionService collectionService;
  private final EventService eventService;

  @Autowired
  public HomeController(CollectionService collectionService, EventService eventService) {
    this.collectionService = collectionService;
    this.eventService = eventService;
  }

  @GetMapping("/")
  public String home() {
    return "redirect:/landing";
  }

  @GetMapping("/landing")
  public String landing(Model model){
    List<CollectionDTO> topCollections = collectionService.getTopCollections();
    model.addAttribute("topCollections", topCollections);
    model.addAttribute("pageTitle", "MetaMapa · Landing");
    return "landing";
  }

  @GetMapping("/explore")
  public String explore(Model model){
    List<EventDTO> events = eventService.getEvents();
    model.addAttribute("events", events);
    model.addAttribute("pageTitle", "MetaMapa · Explore");
    return "explore";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "404";
  }
}
