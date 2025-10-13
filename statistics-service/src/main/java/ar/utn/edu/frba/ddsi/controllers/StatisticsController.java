package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  private final IStatisticsService statisticsService;

  @Autowired
  public StatisticsController(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/collection/{handler}/provinces/top")
  public List<TopOutputDTO> getTopProvincesOnCollection(
      @PathVariable String handler,
      @RequestParam(required = false, defaultValue = "3") Integer count
  ) {
    return statisticsService.findTopProvincesOnCollection(handler, count);
  }

  @GetMapping("/category/{id}/provinces/top")
  public List<TopOutputDTO> getTopProvincesByCategory(
      @PathVariable Long id,
      @RequestParam(required = false, defaultValue = "3") Integer count
  ) {
    return statisticsService.findTopProvincesByCategory(id, count);
  }

  @GetMapping("/categories/top")
  public List<TopOutputDTO> getTopCategories(
      @RequestParam(required = false, defaultValue = "3") Integer count
  ) {
    return statisticsService.findTopCategories(count);
  }

  @GetMapping("/category/{id}/busiest-hours/top")
  public List<String> getBusiestHoursByCategory(
      @PathVariable Long id,
      @RequestParam(required = false, defaultValue = "3") Integer count
  ) {
    return statisticsService.findBusiestHoursByCategory(id, count);
  }
}
