package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import ar.utn.edu.frba.ddsi.services.INormalizationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NormalizationService implements INormalizationService {

  private final ILocationService locationService;

 public NormalizationService(ILocationService locationService) {
    this.locationService = locationService;
  }

  @Override
  // TODO: Esto podría hacerse con un cron task en tiempos de baja demanda o un servicio aparte, pero habría que implementar la lógica para que o no muestre los eventos que todavía no fueron normalizados, o presentarlos como "no normalizados" en el front, algo así.
  public List<Event> eventLocationNormalization(List<Event> events){//TODO Test
    //1. Map events by coordinates (just to cover an extreme case of events with same coordinates on the same request)
    Map<Coordinate, List<Event>> coordenateEventMap = new HashMap<>();
    for (Event event : events) {
      Coordinate coordinate = event.getLocation().getCoordinate();
      //If key not present, create new key with empty list and add event to it, if it exists, just return the value and add the event
      coordenateEventMap.computeIfAbsent(coordinate, k -> new java.util.ArrayList<>()).add(event);
    }
    //2. Get (and persist) the corresponding locations
    List<Location> locations = locationService.findOrCreateLocationByCoordinates(coordenateEventMap.keySet());

    //3. Set the location to the events
    for (Location location : locations) {
      Coordinate coordinate = location.getCoordinate();
      List<Event> eventsForCoordinate = coordenateEventMap.get(coordinate);
      if (eventsForCoordinate != null) {
        for (Event event : eventsForCoordinate) {
          event.setLocation(location);
        }
      }
    }

    //4. Return the events with the normalized locations
    return coordenateEventMap.values().stream().flatMap(List::stream).toList();
  }
}
