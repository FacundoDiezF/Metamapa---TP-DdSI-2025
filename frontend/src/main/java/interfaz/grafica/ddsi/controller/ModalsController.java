package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/modals")
public class ModalsController {

  private final CategoryService categoryService;

  public ModalsController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/more-categories")
  public String getCategoriesModal(
      @RequestParam(defaultValue = "0", name = "page-cat") int page,
      @RequestParam(name = "category_in", required = false)List<Long> categoriesSelected,
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 20);
    model.addAttribute("categoriesSelected", categoriesSelected);
    model.addAttribute("categoriesPage", categoriesPage);
    return "fragments/modals/_filter_moreOptions :: categoriesModal";
  }
}