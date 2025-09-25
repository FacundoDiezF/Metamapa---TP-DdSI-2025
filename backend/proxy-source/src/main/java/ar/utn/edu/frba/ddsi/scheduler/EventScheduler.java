package ar.utn.edu.frba.ddsi.scheduler;

import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {

  @Autowired
  IEventService eventService;

  LocalDateTime lastUpdate = LocalDateTime.now();

  @Scheduled(cron = "${event.notify.cron}")
  public void notifyNewEvents() {
    eventService.notifyEvents(lastUpdate);
    lastUpdate = LocalDateTime.now();
  }
}
