package ar.utn.edu.frba.ddsi.services.impl;

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

//  private final IProvinceRepository provinceRepository;
//  private final ICategoryRepository categoryRepository;
//  private final ICollectionRepository collectionRepository;

  @Autowired
  public StatisticsService(IProvinceRepository provinceRepository, ICategoryRepository categoryRepository, ICollectionRepository collectionRepository) {
//    this.provinceRepository = provinceRepository;
//    this.categoryRepository = categoryRepository;
//    this.collectionRepository = collectionRepository;
  }

  @Override
  public void refreshStatistics(LocalDateTime lastUpdate) {
    //TODO: Implementar, pullear datos del agrgador y popular este modulo con esos datos
  }

  @Override
  public List<TopOutputDTO> findTopCategories(Integer count) {
    return List.of(); //TODO: Implementar
  }

  @Override
  public List<TopOutputDTO> findTopProvinceOnCollection(String collectionHandler, Integer count) {
    return List.of(); //TODO: Implementar
  }

  @Override
  public List<TopOutputDTO> findTopProvinceByCategory(Long categoryId, Integer count) {
    return List.of(); //TODO: Implementar
  }

  @Override
  public List<String> findBusiestHourByCategory(Long categoryId, Integer count) {
    return List.of(); //TODO: Implementar
  }
}
