package ar.utn.edu.frba.ddsi.services;

import java.util.List;

public interface IStatisticsService {

  List<String> findProvinceBy(String id, String handler, Integer count);

  List<String> findTopCategories(Integer count);

  List<String> findTime(String by, String order, Long id, Integer count);
}
