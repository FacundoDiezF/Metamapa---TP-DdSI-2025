package ar.utn.edu.frba.ddsi.services;

import java.time.LocalDateTime;

public interface IEventService {

  void fetchEvents(LocalDateTime lastUpdate);
}
