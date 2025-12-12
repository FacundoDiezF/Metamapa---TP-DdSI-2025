package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.category.CategoryDTO;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final ICategoryService categoryService;

  CategoryController(ICategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public Page<CategoryDTO> getAllCategories(@RequestParam(defaultValue = "0") int page, //TODO analizar cómo impacta al servicio de estadísticas
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(name = "category_in", required = false) List<Long> category_in) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

    if(category_in != null && !category_in.isEmpty()) {
      return categoryService.getAllByIds(category_in, pageable);
    }

    return categoryService.getAll(pageable);
  }

  @GetMapping("/{idCategory}")
  public CategoryDTO getCategoryById(@PathVariable Long idCategory) {
    return categoryService.getById(idCategory);
  }
}
