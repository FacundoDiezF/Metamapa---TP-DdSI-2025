package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Province;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService implements IStatisticsService {

  private final IProvinceRepository provinceRepository;
  private final ICategoryRepository categoryRepository;
  private final ICollectionRepository collectionRepository;

  @Autowired
  public StatisticsService(IProvinceRepository provinceRepository, ICategoryRepository categoryRepository, ICollectionRepository collectionRepository) {
    this.provinceRepository = provinceRepository;
    this.categoryRepository = categoryRepository;
    this.collectionRepository = collectionRepository;
  }

  @Override
  public List<String> findProvinceBy(String by, String id, Integer count) {

    List<Province> provinces = switch (by) {
      case "category" -> {

        if (!categoryRepository.existsById(Long.parseLong(id)))
          throw new NotFoundException("Category with id " + id + " not found");

        //TODO: Agregar throw Bad Request en caso de que tire error el Long.parseLong

        yield provinceRepository.findTopByCategory(Long.parseLong(id), count);

      }
      case "collection" -> {
        if (!collectionRepository.existsByHandler(id))
          throw new NotFoundException("Collection with handler " + id + " not found");

        yield provinceRepository.findTopByCollection(id, count);
      }
      default -> new ArrayList<>();
    };

    return provinces.stream().map(Province::getName).collect(Collectors.toList());
  }

  @Override
  public List<String> findTopCategories(Integer count) {

    List<Category> categories = categoryRepository.findTopCategories(count);
    return categories.stream().map(Category::getName).collect(Collectors.toList());
  }
}
