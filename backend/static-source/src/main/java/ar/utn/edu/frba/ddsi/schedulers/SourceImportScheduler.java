package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.services.ISourceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SourceImportScheduler {

  private final ISourceService sourceService;

  public SourceImportScheduler(ISourceService sourceService) {
    this.sourceService = sourceService;
  }

  @Scheduled(cron = "${import.csv.cron}")
  public void importPendingSources() {
    System.out.println("[CRON] Importing Pending Sources...");

    sourceService.importPendingSources();

    System.out.println("[CRON] END Importing Pending Sources");
  }
}
