package interfaz.grafica.ddsi.models.entitites.adapters;
import interfaz.grafica.ddsi.dtos.EventDTO;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class ServerAdapter {

  private final WebClient webClient;

  public ServerAdapter(@Value("${aggregator.url}") String url) {
    this.webClient = WebClient.builder()
        .baseUrl(url)
        .build();
  }

  public List<EventDTO> getAllEvents(){
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/events")
            //.queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToFlux(EventDTO.class)
        .collectList()
        .block();
  }

  public List<EventDTO> getAllEventsByCollection(String handler) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("collections/"+ handler + "/events")
            //.queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToFlux(EventDTO.class)
        .collectList()
        .block();
  }
}
