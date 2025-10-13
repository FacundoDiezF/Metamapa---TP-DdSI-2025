package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.EventService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

  private final CollectionService collectionService;
  private final EventService eventService;
  private final CategoryService categoryService;

  @Autowired
  public PagesController(CollectionService collectionService, EventService eventService, CategoryService categoryService) {
    this.collectionService = collectionService;
    this.eventService = eventService;
    this.categoryService = categoryService;
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

  @GetMapping("/collections")
  public String collections(Model model){
    List<CollectionDTO> collections = collectionService.getAllCollections();
    model.addAttribute("collections", collections);
    model.addAttribute("pageTitle", "MetaMapa · Colecciones");
    return "collections";
  }

  @GetMapping("/explore")
  public String explore(Model model){
    List<EventDTO> events = eventService.getAllEvents();
    List<CategoryDTO> categories = categoryService.getAllCategories();
    model.addAttribute("events", events);
    model.addAttribute("newEvent", new EventOutputDTO());
    model.addAttribute("categories", categories);
    model.addAttribute("pageTitle", "MetaMapa · Explore");
    return "explore";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "404";
  }
}
