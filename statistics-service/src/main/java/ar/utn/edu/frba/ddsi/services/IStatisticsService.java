package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface IStatisticsService {

  void refreshStatistics(LocalDateTime lastUpdate);

  List<TopOutputDTO> findTopCategories(Integer count);

  List<TopOutputDTO> findTopProvinceOnCollection(String collectionHandler, Integer count);

  List<TopOutputDTO> findTopProvinceByCategory(Long categoryId, Integer count);

  List<String> findBusiestHourByCategory(Long categoryId, Integer count);
}
