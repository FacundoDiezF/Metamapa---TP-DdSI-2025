package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import ar.utn.edu.frba.ddsi.services.INormalizationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class NormalizationService implements INormalizationService {

  private final ILocationService locationService;

 public NormalizationService(ILocationService locationService) {
    this.locationService = locationService;
  }

  @Override
  public List<Event> eventLocationNormalization(List<Event> events){

    Map<Location, List<Event>> locationEventMap = locationService.findOrCreateLocationByEvents(events);

    for (Map.Entry<Location, List<Event>> entry : locationEventMap.entrySet()) {
      Location location = entry.getKey();
      for (Event event : entry.getValue()) {
        event.setLocation(location);
      }
    }

    return events;
  }
}
