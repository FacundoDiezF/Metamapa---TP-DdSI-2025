package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

public class Aggregator implements ISubscriber {

  private final WebClient clientCallBack;

  public Aggregator(String callbackUrl) {
    this.clientCallBack = WebClient
        .builder()
        .baseUrl(callbackUrl)
        .build();
  }

  public static ISubscriber from(String callbackUrl) {
    return new Aggregator(callbackUrl);
  }

  public void notifyNewEvent(List<EventOutputDTO> events) {
    clientCallBack.post()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifyUpdatedEvent(EventOutputDTO outputEvent) {
    clientCallBack.put()
        .uri("/events")
        .bodyValue(List.of(outputEvent)) // El agregador espera una lista
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifySource(SourceOutputDTO dto) {
    clientCallBack.post()
        .uri("/sources")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }
}
