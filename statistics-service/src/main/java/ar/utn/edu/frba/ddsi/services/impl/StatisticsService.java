package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import java.time.LocalDateTime;
import java.util.List;
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
  public void refreshStatistics(LocalDateTime lastUpdate) {
    //TODO: Implementar, pullear datos del agrgador y popular este modulo con esos datos
  }

  @Override
  public List<TopOutputDTO> findTopCategories(Integer count) {
    return categoryRepository.findTopCategories(count);
  }

  @Override
  public List<TopOutputDTO> findTopProvincesOnCollection(String collectionHandler, Integer count) {

    if (!collectionRepository.existsById(collectionHandler)) {
      throw new NotFoundException("Collection not found - Handler: " + collectionHandler);
    }

    return provinceRepository.findTopByCollection(collectionHandler, count);
  }

  @Override
  public List<TopOutputDTO> findTopProvincesByCategory(Long categoryId, Integer count) {

    if (!categoryRepository.existsById(categoryId)) {
      throw new NotFoundException("Category not found - Id: " + categoryId);
    }

    return provinceRepository.findTopByCategory(categoryId, count);
  }

  @Override
  public List<String> findBusiestHoursByCategory(Long categoryId, Integer count) {

    if (!categoryRepository.existsById(categoryId)) {
      throw new NotFoundException("Category not found - Id: " + categoryId);
    }

    return categoryRepository.findCategoryBusiestHours(categoryId, count);
  }
}
