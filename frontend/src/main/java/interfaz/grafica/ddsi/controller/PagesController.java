package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.EventService;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String landing(Model model) {
    List<CollectionDTO> topCollections = collectionService.getTop();
    model.addAttribute("topCollections", topCollections);
    model.addAttribute("pageTitle", "MetaMapa · Landing");
    return "landing";
  }

  @GetMapping("/explore")
  public String explore(
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model) {

    List<EventDTO> events = eventService.getAll(category_in, eventDate_lt, eventDate_gt, source_in);
    List<CategoryDTO> categories = categoryService.getAllCategories();

    // MARKERS para Leaflet (solo eventos con coords)
    List<Map<String, Object>> markers = events.stream()
        .filter(e -> e.getLocation() != null && e.getLocation().getLongitude() != null)
        .map(e -> {
          Map<String, Object> m = new HashMap<>();
          m.put("lat", e.getLocation().getLatitude());
          m.put("lng", e.getLocation().getLongitude());
          m.put("title", e.getTitle());
          m.put("category", e.getCategory());
          m.put("id", e.getId());
          return m;
        }).toList();

    model.addAttribute("events", events);
    model.addAttribute("newEvent", new EventOutputDTO());
    model.addAttribute("categories", categories);
    model.addAttribute("pageTitle", "MetaMapa · Explorar");
    model.addAttribute("active", "explore");
    return "explore";
  }

  @GetMapping("/collection/{handler}")
  public String inCollection(
      @PathVariable String handler,
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model
  ) {
    CollectionDTO collection = collectionService.getByHandler(handler);
    List<EventDTO> eventsFromCol = eventService.getAllByCollection(handler, category_in, eventDate_lt, eventDate_gt, source_in);

    model.addAttribute("collection", collection);
    model.addAttribute("events", eventsFromCol);

    model.addAttribute("pageTitle", "MetaMapa · " + collection.getTitle());
    return "inCollection";
  }

  @GetMapping("/collections")
  public String collections(Model model) {
    List<CollectionDTO> collections = collectionService.getAll();
    model.addAttribute("collections", collections);
    model.addAttribute("pageTitle", "MetaMapa · Colecciones");
    return "collections";
  }

  @GetMapping("/map")
  public String mapa(
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model) {

    List<EventDTO> allEvents = eventService.getAll(category_in, eventDate_lt, eventDate_gt, source_in);
    model.addAttribute("events", allEvents);
    model.addAttribute("pageTitle", "MetaMapa · Mapa");
    return "map";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "404";
  }
}
