package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollectionRefreshScheduler {

  private final IStatisticsService statisticsService;

  private LocalDateTime lastUpdate = LocalDateTime.now();

  @Autowired
  public CollectionRefreshScheduler(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Scheduled(cron = "${statistics.refresh.cron}")
  public void refreshCollections() {

    statisticsService.refreshStatistics(lastUpdate);

    lastUpdate = LocalDateTime.now();
  }
}
