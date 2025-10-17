package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
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
      @RequestParam(defaultValue = "0", name = "page-cat") int pageCat,
      @RequestParam(defaultValue = "0", name = "page") int page,
      @RequestParam(defaultValue = "4", name = "size") int size,
      Model model) {

    PageDTO<EventDTO> eventPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page);
    PageDTO<CategoryDTO> categoryPage = categoryService.getAll(pageCat, 15);
    PageDTO<ProvinceDTO> provincePage = locationService.getAllProvinces(page, size);

    model.addAttribute("events", eventPage.getContent());
    model.addAttribute("newEvent", new EventOutputDTO());

    model.addAttribute("categoryPage", categoryPage);
    model.addAttribute("provinces", provincePage.getContent());

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
  public String collections(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {
    PageDTO<CollectionDTO> collectionPage = collectionService.getAll(page);
    model.addAttribute("collections", collectionPage.getContent());
    model.addAttribute("pageTitle", "MetaMapa · Colecciones");
    model.addAttribute("active", "collections");
    return "collections";
  }

  @GetMapping("/map")
  public String mapa(
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      @RequestParam(defaultValue = "0", name = "page") int page,
      Model model) {

    PageDTO<EventDTO> eventPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page); //? Ahora mismo el mapa solo muestra los eventos de la pagina que trae
    model.addAttribute("events", eventPage.getContent());
    model.addAttribute("pageTitle", "MetaMapa · Mapa");
    model.addAttribute("active", "map");
    return "map";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "error/404";
  }
}
