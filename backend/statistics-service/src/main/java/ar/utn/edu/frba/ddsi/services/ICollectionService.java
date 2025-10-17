package ar.utn.edu.frba.ddsi.services;

import java.time.LocalDateTime;

public interface ICollectionService {

  void fetchCollections(LocalDateTime lastUpdate);
}
