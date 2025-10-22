package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import interfaz.grafica.ddsi.services.LocationService;
import java.util.List;
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
      @RequestParam(name = "category_in", required = false) List<Long> categoriesSelected,
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 20);
    model.addAttribute("categoriesSelected", categoriesSelected);
    model.addAttribute("categoriesPage", categoriesPage);
    return "fragments/modals/_filter_moreOptions :: categoriesModal";
  }

  @GetMapping("/more-provinces")
  public String getMoreProvincesModal(
      @RequestParam(defaultValue = "0", name = "page-prov") int page,
      @RequestParam(name = "province_in", required = false) List<Long> provincesSelected,
      Model model) {

    PageDTO<ProvinceDTO> provincePage = locationService.getAllProvinces(page, 24);
    model.addAttribute("provincesSelected", provincesSelected);
    model.addAttribute("provincePage", provincePage);
    return "fragments/modals/_filter_moreOptions :: provincesModal";
  }

  @GetMapping("/report-event/{id}")
  public String getReportEventModal(
      @PathVariable Long id,
      Model model) {

    DeletionRequestDTO deletionRequestDTO = new DeletionRequestDTO();
    deletionRequestDTO.setEventId(id);

    model.addAttribute("newDeletionRequest", deletionRequestDTO);

    return "fragments/modals/_reportEventModal :: deletionRequestModal";
  }
}