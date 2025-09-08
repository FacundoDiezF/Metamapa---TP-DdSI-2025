package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  private final IStatisticsService statisticsService;

  @Autowired
  public StatisticsController(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/province")
  public List<String> getProvinceBy(
      @RequestParam(required = true) String by,
      @RequestParam(required = true) String id,
      @RequestParam(required = false, defaultValue = "desc") String order,
      @RequestParam(required = false, defaultValue = "1") Integer count
      ) {
    return statisticsService.findProvinceBy(by, id, order, count);
  }

}
