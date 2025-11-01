package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.*;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.*;
import interfaz.grafica.ddsi.services.StatisticsService;

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
  private final StatisticsService statisticsService;
  private final SourcesService sourcesService;


    @Autowired
  public PagesController(CollectionService collectionService, EventService eventService, CategoryService categoryService, LocationService locationService, StatisticsService statisticsService, SourcesService sourcesService) {
    this.collectionService = collectionService;
    this.eventService = eventService;
    this.categoryService = categoryService;
    this.locationService = locationService;
    this.statisticsService = statisticsService;
        this.sourcesService = sourcesService;
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

  //TODO: Query params con nombres correctos
  @GetMapping("/explore")
  public String explore(
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      @RequestParam(defaultValue = "0", name = "page") int pageEvent,
      Model model) {

    PageDTO<EventDTO> eventsPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, pageEvent, 15);



    model.addAttribute("eventsPage", eventsPage);
    model.addAttribute("newEvent", new EventOutputDTO());

    this.addFilterModels(model, category_in, province_in);

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
      @RequestParam(defaultValue = "0", name = "page") int pageEvent,
      Model model) {

    CollectionDTO collection = collectionService.getByHandler(handler);
    List<EventDTO> eventsFromCol = eventService.getAllByCollection(handler, category_in, province_in, eventDate_lt, eventDate_gt, source_in, pageEvent, 15);


    model.addAttribute("collection", collection);

    model.addAttribute("events", eventsFromCol);

    this.addFilterModels(model, category_in, province_in);

    model.addAttribute("pageTitle", "MetaMapa · " + collection.getTitle());
    model.addAttribute("active", "collections");
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

    PageDTO<EventDTO> eventPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page, 100);

    model.addAttribute("eventsPage", eventPage);

    this.addFilterModels(model, category_in, province_in);

    model.addAttribute("pageTitle", "MetaMapa · Mapa");
    model.addAttribute("active", "map");
    return "map";
  }

  @GetMapping("/404")
  public String notFound(Model model) {
    model.addAttribute("titulo", "No encontrado");
    return "error/404";
  }

  private void addFilterModels(Model model, List<Long> category_in, List<Long> province_in) {

    List<CategoryDTO> selectedCategories = categoryService.getByIds(category_in);
    List<ProvinceDTO> selectedProvinces = locationService.getProvincesByIds(province_in);

    model.addAttribute("selectedCategories", selectedCategories);
    model.addAttribute("selectedProvinces", selectedProvinces);
  }


    @GetMapping("/admin/collections")
    public String adminCollections(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {


        PageDTO<CollectionDTO> collectionPage = collectionService.getAll(page);

        model.addAttribute("collections", collectionPage.getContent());

        model.addAttribute("pageTitle", "MetaMapa · Colecciones");
        model.addAttribute("active", "admin");



        return "admin/pages/collectionsSection";
    }

    @GetMapping("/admin/statistics")
    public String adminStatistic(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {


        List<StatisticsDTO> topCategories = statisticsService.findTopCategories();
        List<StatisticsDTO> TopProvincesOnCollection = statisticsService.findTopProvincesOnCollection();
        List<StatisticsDTO> TopProvincesByCategory = statisticsService.findTopProvincesByCategory();
        List<StatisticsDTO> topHours = statisticsService.findBusiestHoursByCategory();

        model.addAttribute("active", "admin");
        model.addAttribute("topCategories", topCategories);
        model.addAttribute("TopProvincesOnCollection", TopProvincesOnCollection);
        model.addAttribute("TopProvincesByCategory", TopProvincesByCategory);
        model.addAttribute("topHours", topHours);

        return "admin/pages/statisticsSection";
    }

    @GetMapping("/admin/sources")
    public String adminSources(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {

        List<SourceDTO> allSources = sourcesService.getAll();

        model.addAttribute("active", "admin");
        model.addAttribute("sources", allSources);

        return "admin/pages/sourcesSection";
    }
}


