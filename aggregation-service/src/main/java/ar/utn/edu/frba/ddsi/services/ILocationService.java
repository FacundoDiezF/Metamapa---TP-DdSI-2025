package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;

import java.util.List;
import java.util.Map;

public interface ILocationService {
  Location findOrCreate(Double latitude, Double longitude);

  Map<Location, List<Event>> findOrCreateLocationByEvents(List<Event> events);
}
