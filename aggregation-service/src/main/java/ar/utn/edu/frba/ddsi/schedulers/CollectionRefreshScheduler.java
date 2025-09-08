package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.services.ICollectionService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CollectionRefreshScheduler {

  private final  ICollectionService collectionService;

  private LocalDateTime lastUpdate = LocalDateTime.now();

  @Autowired
  public CollectionRefreshScheduler(ICollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @Scheduled(cron = "${collection.refresh.cron}")
  public void refreshCollections() {

    collectionService.refreshCollections(lastUpdate);

    lastUpdate = LocalDateTime.now();
  }
}
