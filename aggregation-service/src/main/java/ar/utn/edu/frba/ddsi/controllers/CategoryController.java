package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final ICategoryService categoryService;

  CategoryController(ICategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public List<Category> getAllCategories() {
    return categoryService.getAll();
  }
}
