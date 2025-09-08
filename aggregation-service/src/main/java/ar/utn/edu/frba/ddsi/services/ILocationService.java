package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;

public interface ILocationService {
  Location findOrCreate(Double latitude, Double longitude);
}
