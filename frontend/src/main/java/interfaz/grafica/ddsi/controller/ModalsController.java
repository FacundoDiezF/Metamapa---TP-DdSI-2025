package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
import interfaz.grafica.ddsi.dtos.output.SourceClientOutputDTO;
import interfaz.grafica.ddsi.dtos.output.collection.CollectionOutputDTO;
import interfaz.grafica.ddsi.dtos.output.collection.ConsensusType;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.CollectionService;
import interfaz.grafica.ddsi.services.LocationService;
import interfaz.grafica.ddsi.services.SourcesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/modals")
public class ModalsController {

  private final CategoryService categoryService;
  private final LocationService locationService;
  private final SourcesService sourcesService;
  private final CollectionService collectionService;

  public ModalsController(CategoryService categoryService, LocationService locationService, SourcesService sourcesService, CollectionService collectionService) {
    this.categoryService = categoryService;
    this.locationService = locationService;
    this.sourcesService = sourcesService;
    this.collectionService = collectionService;
  }

  @GetMapping("/more-categories")
  public String getMoreCategoriesModal(
      @RequestParam(defaultValue = "0", name = "page-cat") int page,
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 20);
    model.addAttribute("categoriesPage", categoriesPage);
    model.addAttribute("elementName", "categorias");
    model.addAttribute("filterKey", "category_in");
    model.addAttribute("modalPagePath", "/modals/more-categories");
    return "fragments/modals/_moreOptions :: moreOptionsFilterModal(itemsPage=${categoriesPage}, elementName=${elementName}, filterKey=${filterKey}, modalPagePath=${modalPagePath})";
  }

  @GetMapping("/one-category")
  public String getOneCategoryModal(
      @RequestParam(defaultValue = "0", name = "page-cat") int page,
      @RequestParam(value = "renderMode", defaultValue = "chip") String renderMode,
      @RequestParam(value = "targetContainer", defaultValue = "#selected-category") String targetContainer,
      @RequestParam(value = "redirectBase", required = false) String redirectBase,
      @RequestParam(value = "inputName", required = false) String inputName,
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 20);
    model.addAttribute("categoriesPage", categoriesPage);
    model.addAttribute("elementName", "categorias");
    model.addAttribute("renderMode", renderMode);
    model.addAttribute("targetContainer", targetContainer);
    model.addAttribute("modalPagePath", "/modals/one-category");
    model.addAttribute("redirectBase", redirectBase);
    model.addAttribute("inputName", inputName);

    return "fragments/modals/_moreOptions :: oneChipElementModal(itemsPage=${categoriesPage}, elementName=${elementName}, modalPagePath=${modalPagePath})";
  }

  @GetMapping("/map-selection")
  public String getMapModal(
      @RequestParam(value = "currentLat", required = false) Double currentLat,
      @RequestParam(value = "currentLng", required = false) Double currentLng,
      @RequestParam(value = "targetContainer", defaultValue = "#selected-location") String targetContainer,
      @RequestParam(value = "renderMode", defaultValue = "chip") String renderMode,
      Model model
  ) {
    model.addAttribute("currentLat", currentLat);
    model.addAttribute("currentLng", currentLng);
    model.addAttribute("targetContainer", targetContainer);
    model.addAttribute("renderMode", renderMode);

    return "fragments/modals/_mapModal :: mapModal";
  }

  @GetMapping("/more-provinces")
  public String getMoreProvincesModal(
      @RequestParam(defaultValue = "0", name = "page-prov") int page,
      Model model) {

    PageDTO<ProvinceDTO> provincePage = locationService.getAllProvinces(page, 24);
    model.addAttribute("provincePage", provincePage);
    model.addAttribute("elementName", "provincias");
    model.addAttribute("filterKey", "province_in");
    model.addAttribute("modalPagePath", "/modals/more-provinces");
    return "fragments/modals/_moreOptions :: moreOptionsFilterModal(itemsPage=${provincePage}, elementName=${elementName}, filterKey=${filterKey}, modalPagePath=${modalPagePath})";
  }

  @GetMapping("/report-event/{id}")
  public String getReportEventModal(
      @PathVariable Long id,
      Model model) {

    DeletionRequestCreationDTO deletionRequestDTO = new DeletionRequestCreationDTO();
    deletionRequestDTO.setEventId(id);

    model.addAttribute("newDeletionRequest", deletionRequestDTO);

    return "fragments/modals/_reportEventModal :: deletionRequestModal";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/new-collection")
  public String getNewCollectionModal(Model model) {

    CollectionOutputDTO form = new CollectionOutputDTO();
    form.setConsensusType(ConsensusType.ALL_PASS);

    model.addAttribute("newCollection", form);
    model.addAttribute("sources", sourcesService.getAll());

    return "fragments/modals/_newCollectionModal :: newCollectionModal";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/edit-collection/{handler}")
  public String editNewCollectionModal(@PathVariable String handler, Model model) {

    //Obtengo la coleccion y la cargo en el formulario (cambio el formato del DTO)
    CollectionDTO collectionToEdit = collectionService.getByHandler(handler);

    CollectionOutputDTO form = new CollectionOutputDTO();
    form.setTitle(collectionToEdit.getTitle());
    form.setDescription(collectionToEdit.getDescription());
    form.setConsensusType(collectionToEdit.getConsensusType());
    form.setConditions(collectionToEdit.getConditions());
    form.setSourceIds(collectionToEdit.getSources().stream().map(SourceDTO::getId).toList());


    model.addAttribute("newCollection", form);
    model.addAttribute("sources", sourcesService.getAll());

    model.addAttribute("isEditMode", true);
    model.addAttribute("handler", handler);

    return "fragments/modals/_newCollectionModal :: newCollectionModal";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/confirm-delete/{handler}")
  public String getConfirmDeleteModal(@PathVariable String handler, Model model) {

    CollectionDTO col = collectionService.getByHandler(handler);

    model.addAttribute("handler", handler);
    model.addAttribute("title", col.getTitle());

    return "fragments/modals/_messageModals :: deleteConfirmationModal";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/new-source")
  public String getNewSourceModal(Model model) {

    model.addAttribute("newSourceClient", new SourceClientOutputDTO());

    return "fragments/modals/_newSourceModal :: newSourceModal";
  }
}