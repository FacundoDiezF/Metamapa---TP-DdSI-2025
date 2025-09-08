package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Province;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService implements IStatisticsService {

  IProvinceRepository provinceRepository;

  public StatisticsService(IProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  public List<String> findProvinceBy(String by, String id, String order, Integer count) {
    //List<Province> provinces = provinceRepository.findProvinceBy(by, id, order, count);

    return new ArrayList<>(); //TODO
  }
}
