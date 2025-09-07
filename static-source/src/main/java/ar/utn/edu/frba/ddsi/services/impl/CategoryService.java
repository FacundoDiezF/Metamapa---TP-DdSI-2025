package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
  private final ICategoryRepository categoryRepository;

  private CategoryService(ICategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category findOrCreate(String categoryName) {
    // Aca si queremos podemos normalizar y esas cosas
    return categoryRepository.findByName(categoryName)
        .orElseGet(()-> categoryRepository.save(new Category(categoryName)));
  }
}
