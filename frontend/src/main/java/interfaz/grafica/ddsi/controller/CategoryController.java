package interfaz.grafica.ddsi.controller;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.services.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping("/modal")
  public String getCategoriesModal(
      @RequestParam(defaultValue = "0", name = "page-cat") int page,
      Model model) {

    PageDTO<CategoryDTO> categoriesPage = categoryService.getAll(page, 15);
    model.addAttribute("categoriesPage", categoriesPage);
    return "fragments/modals/_filter_moreOptions :: categoriesModal(categoriesPage= ${categoriesPage})";
  }
}