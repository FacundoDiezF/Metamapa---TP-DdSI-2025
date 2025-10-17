package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import ar.utn.edu.frba.ddsi.services.IEventService;
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
  private final IEventService eventService;
  private final ICollectionService collectionService;

  @Autowired
  public StatisticsService(IProvinceRepository provinceRepository, ICategoryRepository categoryRepository, ICollectionRepository collectionRepository, IEventService eventService, ICollectionService collectionService) {
    this.provinceRepository = provinceRepository;
    this.categoryRepository = categoryRepository;
    this.collectionRepository = collectionRepository;
    this.eventService = eventService;
    this.collectionService = collectionService;
  }

  @Override
  public void refreshStatistics(LocalDateTime lastUpdate) {
    try {
      eventService.fetchEvents(lastUpdate);
    } catch (Exception e) {
      throw new RuntimeException("Error fetching events", e);
    }

    try {
      collectionService.fetchCollections(lastUpdate);
    } catch (Exception e) {
      throw new RuntimeException("Error fetching collections", e);
    }
  }

  //----- Statistics methods (maybe move to their oun service)

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
