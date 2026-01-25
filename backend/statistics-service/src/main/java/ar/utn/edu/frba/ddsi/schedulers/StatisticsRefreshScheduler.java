package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.services.IStatisticsService;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsRefreshScheduler {

  private final IStatisticsService statisticsService;

  private LocalDateTime lastUpdate;

  @Autowired
  public StatisticsRefreshScheduler(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Scheduled(cron = "${statistics.refresh.cron}")
  @PostConstruct
  public void refreshStatistics() {
    System.out.println("[CRON] Refreshing Stats...");

    statisticsService.refreshStatistics(lastUpdate);
    lastUpdate = LocalDateTime.now();

    System.out.println("[CRON] END Refreshing Stats");
  }
}
