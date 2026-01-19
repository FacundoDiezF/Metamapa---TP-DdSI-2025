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

  @Scheduled(fixedDelayString = "${imports.fixed-delay-ms:10000}")
  public void importPendingSources() {
    System.out.println("[CRON] Ejecutando importPendingSources...");
    sourceService.importPendingSources();
  }
}
