package ar.utn.edu.frba.ddsi.models.entities.adapters;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LocationAPI {
  WebClient webClient;

  public LocationAPI(@Value("${geo-ref.url}") String url) {
    this.webClient = WebClient.builder().baseUrl(url).build();
  }

  public Map<Location, List<Event>> getLocationsByEvents(List<Event> events) {
    // TODO Implementar llamada a la API de georef
    List<List<Event>> batches = this.chunkEvents(events, 1000);
    for (List<Event> batch : batches) {
    }


      return null;
  }

  private List<List<Event>> chunkEvents(List<Event> events, int batchSize) {
    List<List<Event>> batches = new ArrayList<>();
    for (int i = 0; i < events.size(); i += batchSize) {
      batches.add(events.subList(i, Math.min(i + batchSize, events.size())));
    }
    return batches;
  }
}
