package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsScheduler {

  private final IStatisticsService statisticsService;

  public StatisticsScheduler(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Scheduled(cron = "${statistics.refresh.cron}")
  public void recalculateStatistics() {
    // TODO
  }
}
