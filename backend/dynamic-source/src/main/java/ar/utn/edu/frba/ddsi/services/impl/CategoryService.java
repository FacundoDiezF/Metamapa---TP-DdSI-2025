package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {
  private final ICategoryRepository categoryRepository;

  private static final double SIMILARITY_THRESHOLD = 0.8;

  public CategoryService(ICategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Override
  public Category findOrCreate(String categoryName) {

    // Search for a matching category using fuzzy matching.
    String cleanName = cleanCategoryName(categoryName);
    String normalizedName = findFuzzyMatch(cleanName);

    return categoryRepository.findByName(normalizedName)
        .orElseGet(() -> categoryRepository.save(new Category(normalizedName)));

  }

  private String findFuzzyMatch(String name) {
    LevenshteinDistance levDist = new LevenshteinDistance();
    double bestScore = 0;
    String bestMatch = name;

    // We get the list of canonical categories from our cached method.
    for (Category canonicalCategory : categoryRepository.findAll()) { //? A lo mejor combiene optimizar y guarde en cache las categorias canonicas, asi no va al repo

      String canonicalName = canonicalCategory.getName();
      int maxLen = Math.max(name.length(), canonicalName.length());
      if (maxLen == 0) continue;

      double similarity = 1.0 - ((double) levDist.apply(name.toLowerCase(), canonicalName.toLowerCase()) / maxLen);

      if (similarity > bestScore) {
        bestScore = similarity;
        bestMatch = canonicalName;
      }
    }

    return (bestScore >= SIMILARITY_THRESHOLD) ? bestMatch : name;
  }

  // Normalizes category name. (First words letters uppercase)
  private String cleanCategoryName(String name) {

    if (name == null || name.trim().isEmpty()) {
      return "Sin Categoría";
    }

    StringBuilder result = new StringBuilder();

    String[] words = name.trim().split("\\s+");
    for (String word : words) {
      if (!word.isEmpty()) {
        if (!result.isEmpty()) result.append(" ");
        result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
      }
    }
    return result.toString();
  }
}
