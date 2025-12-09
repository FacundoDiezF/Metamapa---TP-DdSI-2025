package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import interfaz.grafica.ddsi.dtos.SubmissionRequestDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.DeletionRequestService;
import interfaz.grafica.ddsi.services.EventService;
import interfaz.grafica.ddsi.services.LocationService;
import interfaz.grafica.ddsi.services.SourcesService;
import interfaz.grafica.ddsi.services.StatisticsService;
import interfaz.grafica.ddsi.services.SubmissionRequestService;
import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
  private final DeletionRequestService deletionRequestService;
  private final SubmissionRequestService submissionRequestService;


  @Autowired
  public PagesController(CollectionService collectionService, EventService eventService, CategoryService categoryService, LocationService locationService, StatisticsService statisticsService, SourcesService sourcesService, DeletionRequestService deletionRequestService, SubmissionRequestService submissionRequestService) {
    this.collectionService = collectionService;
    this.eventService = eventService;
    this.categoryService = categoryService;
    this.locationService = locationService;
    this.statisticsService = statisticsService;
    this.sourcesService = sourcesService;
    this.deletionRequestService = deletionRequestService;
    this.submissionRequestService = submissionRequestService;
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
      @RequestParam(defaultValue = "0", name = "page") int pageEvent,
      Model model,
      HttpServletRequest request) {

    // -----------------------------------------------------------------------------
    // [FIX CRÍTICO PARA THYMELEAF + SPRING SECURITY]
    // Problema: Thymeleaf empieza a escribir el HTML de la respuesta (streaming) antes
    // de que Spring Security intente crear el token CSRF para los formularios.
    // Como el CSRF requiere una sesión, Spring intenta crearla e inyectar la cookie
    // JSESSIONID, pero falla porque los headers ya se enviaron ("Response committed").
    //
    // Solución: Forzamos la creación de la sesión acá, antes de que se procese
    // cualquier vista, para asegurar que la cookie de sesión viaje en los headers.
    //
    // Repitan eso en otros endpoints si les llega a salir el error IllegalStateException: Cannot create a session bla bla bla.
    // Si encuentran una mejor solución, reemplacen esta.
    // -----------------------------------------------------------------------------
    //request.getSession(true);

    PageDTO<EventDTO> eventsPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, pageEvent, 15);

    PageDTO<CollectionDTO> collectionPage = collectionService.getAll(0, 3);


    model.addAttribute("eventsPage", eventsPage);
    model.addAttribute("newEvent", new EventOutputDTO());
    model.addAttribute("collectionPage", collectionPage);

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
      @RequestParam(required = false, name = "curatedNavigation") boolean curatedNavigation,
      @RequestParam(defaultValue = "0", name = "page") int pageEvent,
      Model model) {

    CollectionDTO collection = collectionService.getByHandler(handler);
    List<EventDTO> eventsFromCol = eventService.getAllByCollection(handler, category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, pageEvent, 15);
    PageDTO<CollectionDTO> collectionPage = collectionService.getAll(0, 3);


    model.addAttribute("collection", collection);
    model.addAttribute("collectionPage", collectionPage);

    model.addAttribute("events", eventsFromCol);

    this.addFilterModels(model, category_in, province_in);

    model.addAttribute("pageTitle", "MetaMapa · " + collection.getTitle());
    model.addAttribute("active", "collections");
    return "inCollection";
  }

  @GetMapping("/collections")
  public String collections(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {

    PageDTO<CollectionDTO> collectionPage = collectionService.getAll(page, 20);

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

    List<CategoryDTO> selectedCategories = categoryService.getAllByIds(category_in);
    List<ProvinceDTO> selectedProvinces = locationService.getProvincesByIds(province_in);

    model.addAttribute("selectedCategories", selectedCategories);
    model.addAttribute("selectedProvinces", selectedProvinces);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/collections")
  public String adminCollections(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {


    PageDTO<CollectionDTO> collectionPage = collectionService.getAll(page, 20);

    model.addAttribute("collections", collectionPage.getContent());

    model.addAttribute("pageTitle", "MetaMapa · Gestionar Colecciones");
    model.addAttribute("active", "admin");


    return "admin/pages/collectionsSection";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/statistics")
  public String adminStatistic(Model model,
                               @RequestParam(defaultValue = "1", name = "catId") Long selectedCategoryId) {

    List<StatisticsDTO> topCategories = statisticsService.findTopCategories();
    List<StatisticsDTO> TopProvincesOnCollection = statisticsService.findTopProvincesOnCollection();

    CategoryDTO selectedCategory = categoryService.getById(selectedCategoryId);
    List<StatisticsDTO> TopProvincesByCategory = statisticsService.findTopProvincesByCategory(selectedCategoryId);
    List<StatisticsDTO> topHours = statisticsService.findBusiestHoursByCategory(selectedCategoryId);

    model.addAttribute("topCategories", topCategories);
    model.addAttribute("TopProvincesOnCollection", TopProvincesOnCollection);

    model.addAttribute("selectedCategory", selectedCategory);
    model.addAttribute("TopProvincesByCategory", TopProvincesByCategory);
    model.addAttribute("topHours", topHours);

    model.addAttribute("active", "admin");
    model.addAttribute("pageTitle", "MetaMapa · Estadisticas");


    return "admin/pages/statisticsSection";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/sources")
  public String adminSources(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {

    List<SourceDTO> allSources = sourcesService.getAll();

    model.addAttribute("sources", allSources);

    model.addAttribute("active", "admin");
    model.addAttribute("pageTitle", "MetaMapa · Gestionar Fuentes");


    return "admin/pages/sourcesSection";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/pendingEvents")
  public String adminPendingEvents(
      @RequestParam(defaultValue = "0", name = "page") int page,
      Model model) {

    PageDTO<SubmissionRequestDTO> submissionPage = submissionRequestService.getAll(page, 15);

    model.addAttribute("submissionPage", submissionPage);

    model.addAttribute("active", "admin");
    model.addAttribute("pageTitle", "MetaMapa · Hechos Pendientes");

    return "admin/pages/pendingEventsSection";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin/deletionRequests")
  public String adminDeletionRequests(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {

    PageDTO<DeletionRequestDTO> deletionsRequestsPage = deletionRequestService.getAll(page, 5);

    model.addAttribute("deletionsRequestsPage", deletionsRequestsPage);


    model.addAttribute("active", "admin");
    model.addAttribute("pageTitle", "MetaMapa · Gestionar Solicitudes de Eliminación");

    return "admin/pages/deletionRequestsSection";
  }


}


