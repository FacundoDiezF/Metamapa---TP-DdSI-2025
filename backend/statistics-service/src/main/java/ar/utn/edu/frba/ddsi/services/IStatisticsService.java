package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface IStatisticsService {

  void refreshStatistics(LocalDateTime lastUpdate);

  List<TopOutputDTO> findTopCategories(Integer count);

  List<TopOutputDTO> findTopProvincesOnCollection(String collectionHandler, Integer count);

  List<TopOutputDTO> findTopProvincesByCategory(Long categoryId, Integer count);

  List<String> findBusiestHoursByCategory(Long categoryId, Integer count);
}
