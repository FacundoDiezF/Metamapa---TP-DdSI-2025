package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
import interfaz.grafica.ddsi.dtos.output.SourceClientOutputDTO;
import interfaz.grafica.ddsi.dtos.output.collection.CollectionOutputDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.LocationService;
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

  public ModalsController(CategoryService categoryService, LocationService locationService) {
    this.categoryService = categoryService;
    this.locationService = locationService;
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
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 20);
    model.addAttribute("categoriesPage", categoriesPage);
    model.addAttribute("elementName", "categorias");
    model.addAttribute("modalPagePath", "/modals/one-category");
    return "fragments/modals/_moreOptions :: oneChipElementModal(itemsPage=${categoriesPage}, elementName=${elementName}, modalPagePath=${modalPagePath})";
  }

  @GetMapping("/map-selection")
  public String getMapModal() {
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

  @GetMapping("/new-collection")
  public String getNewCollectionModal(Model model) {

    model.addAttribute("newCollection", new CollectionOutputDTO());

    return "fragments/modals/_newCollectionModal :: newCollectoinModal";
  }

  @GetMapping("/new-source")
  public String getNewSourceModal(Model model) {

    model.addAttribute("newSourceClient", new SourceClientOutputDTO());

    return "fragments/modals/_newSourceModal :: newSourceModal";
  }
}