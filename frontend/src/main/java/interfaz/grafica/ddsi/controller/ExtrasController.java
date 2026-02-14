package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.collection.ConditionDTO;
import interfaz.grafica.ddsi.dtos.output.collection.ConditionType;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.EventService;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ExtrasController {

  private final CategoryService categoryService;

  private final EventService eventService;

  public ExtrasController(CategoryService categoryService, EventService eventService) {
    this.categoryService = categoryService;
    this.eventService = eventService;
  }

  // Devuelve el fragmento HTML con la categoria seleccionada (new Event Form)
  @PostMapping("/category-input-fragment")
  public String getCategoryInputFragment(
      @RequestParam("itemSelected") String item,
      @RequestParam(value = "renderMode", defaultValue = "chip") String renderMode,
      @RequestParam(value = "redirectBase", required = false) String redirectBase,
      @RequestParam(value = "inputName", required = false) String inputName,
      HttpServletResponse response,
      Model model) {

    String[] parts = item.split("::");
    Long catId = Long.valueOf(parts[0]);
    String catName = parts[1];

    model.addAttribute("catName", catName);
    model.addAttribute("catId", catId);

    // Dynamic fragment style based on renderMode
    if ("badge".equals(renderMode)) {
      return "fragments/modals/_moreOptions :: categoryBadgeForEdit(catId=${catId}, catName=${catName})";
    }

    if ("collectionBadge".equals(renderMode)) {
      model.addAttribute("inputName", inputName); // To set the same dynamic name (conditions[x]...)
      return "fragments/modals/_moreOptions :: collectionConditionBadge(catId=${catId}, catName=${catName}, inputName=${inputName})";
    }

    if ("redirect".equals(renderMode) && redirectBase != null) {
      String finalUrl = redirectBase + catId;
      response.setHeader("HX-Redirect", finalUrl);
      return ""; // No fragment to render, just a redirect
    }
    return "fragments/modals/_moreOptions :: categoryBasicChip(catId=${catId}, catName=${catName})";
  }

  // Devuelve el fragmento HTML con la categoria seleccionada (new Collection Form)
  @GetMapping("/category-input-fragment/render-badge")
  public String renderBadgeFromId(
      @RequestParam("id") Long catId,
      @RequestParam("inputName") String inputName,
      Model model
  ) {
    // 1. Buscamos el nombre real en la DB porque solo tenemos el ID
    CategoryDTO category = categoryService.getById(catId);

    // 2. Cargamos los atributos que espera el fragmento
    model.addAttribute("catId", category.getId());
    model.addAttribute("catName", category.getName());
    model.addAttribute("inputName", inputName);

    // 3. REUTILIZAMOS el mismo fragmento visual que usa la modal
    return "fragments/modals/_moreOptions :: collectionConditionBadge(catId=${catId}, catName=${catName}, inputName=${inputName})";
  }

  // Devuelve el fragmento HTML con la ubicación seleccionada en el mapa (new Event Form)
  @PostMapping("/location-input-fragment")
  public String getLocationInputFragment(
      @RequestParam("latitude") Double latitude,
      @RequestParam("longitude") Double longitude,
      @RequestParam(value = "renderMode", defaultValue = "chip") String renderMode,
      Model model) {

    model.addAttribute("lat", latitude);
    model.addAttribute("lng", longitude);

    if ("badge".equals(renderMode)) {
      return "fragments/modals/_mapModal :: locationBadgeForEdit";
    }

    return "fragments/modals/_mapModal :: locationBasicChip";
  }

  // Agrega una nueva fila de condición al formulario de creación de colección
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/new-collection/add-condition-row")
  public String addConditionRow(@RequestParam("type") ConditionType type,
                                @RequestParam("index") int index,
                                Model model) {

    ConditionDTO conditionDto = new ConditionDTO();
    conditionDto.setConditionType(type);

    model.addAttribute("condition", conditionDto);
    model.addAttribute("idx", index); //Para th:field (conditions[0], conditions[1]...)

    return "fragments/modals/_newCollectionModal :: conditionRow";
  }

  @GetMapping("/explore/events")
  public String exploreEventsPaginated(
      @RequestParam(required = false, name = "category_in") List<Long> category_in,
      @RequestParam(required = false, name = "province_in") List<Long> province_in,
      @RequestParam(required = false, name = "eventDate_lt") LocalDate eventDate_lt,
      @RequestParam(required = false, name = "eventDate_gt") LocalDate eventDate_gt,
      @RequestParam(required = false, name = "source_in") List<Origin> source_in,
      @RequestParam(defaultValue = "0", name = "page") int pageEvent,
      Model model) {

    PageDTO<EventDTO> eventsPage = eventService.getAll(category_in, province_in, eventDate_lt, eventDate_gt, source_in, pageEvent, 15);

    model.addAttribute("eventsPage", eventsPage);

    return "fragments/grid3/center/_explore_center :: eventListFragment";
  }
}
