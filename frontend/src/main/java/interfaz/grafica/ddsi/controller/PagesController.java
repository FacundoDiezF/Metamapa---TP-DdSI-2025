package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.EventService;
import interfaz.grafica.ddsi.services.LocationService;
import java.time.LocalDate;
import java.util.List;
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
  private final LocationService locationService;

  @Autowired
  public PagesController(CollectionService collectionService, EventService eventService, CategoryService categoryService, LocationService locationService) {
    this.collectionService = collectionService;
    this.eventService = eventService;
    this.categoryService = categoryService;
    this.locationService = locationService;
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
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model) {

    List<EventDTO> events = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in);
    List<CategoryDTO> categories = categoryService.getAllCategories();
    List<ProvinceDTO> provinces = locationService.getAllProvinces();

    model.addAttribute("events", events);
    model.addAttribute("newEvent", new EventOutputDTO());

    model.addAttribute("categories", categories);
    model.addAttribute("provinces", provinces);

    model.addAttribute("pageTitle", "MetaMapa · Explorar");
    model.addAttribute("active", "explore");
    return "explore";
  }

  @GetMapping("/collection/{handler}")
  public String inCollection(
      @PathVariable String handler,
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model
  ) {
    CollectionDTO collection = collectionService.getByHandler(handler);
    List<EventDTO> eventsFromCol = eventService.getAllByCollection(handler, category_in, province_in, eventDate_lt, eventDate_gt, source_in);

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
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      Model model) {

    List<EventDTO> allEvents = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in);
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
