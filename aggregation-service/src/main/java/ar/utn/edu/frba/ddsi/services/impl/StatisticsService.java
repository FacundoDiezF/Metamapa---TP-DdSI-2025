package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService implements IStatisticsService {

  IProvinceRepository provinceRepository;

  public StatisticsService(IProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public List<String> findProvinceBy(String by, String id, String order, Integer count) {
    return provinceRepository.findProvinceBy(by, id, order, count);
  }

//  @Override
//  public String findCategoryWithMostEvents() {
//    return statisticsRepository.findCategoryWithMostEvents();
//  }
//
//  @Override
//  public String findProvinceWithMostEventsByCategory(Long categoryId) {
//    return statisticsRepository.findProvinceWithMostEventsByCategory(categoryId);
//  }
//
//  @Override
//  public String findPeakHourByCategory(Long categoryId) {
//    return statisticsRepository.findPeakHourByCategory(categoryId);
//  }
//
//  @Override
//  public String findDeletionRequestSpamCount() {
//    return statisticsRepository.findDeletionRequestSpamCount();
//  }}
}
