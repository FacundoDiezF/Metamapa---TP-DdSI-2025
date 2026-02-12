package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

  @Override
  public Mono<Void> notifyNewEvent(List<EventOutputDTO> events) {
    return clientCallBack.post()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<Void> notifyUpdatedEvent(EventOutputDTO outputEvent) {
    return clientCallBack.put()
        .uri("/events")
        .bodyValue(List.of(outputEvent))
        .retrieve()
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<Void> notifySource(SourceOutputDTO dto) {
    return clientCallBack.post()
        .uri("/sources")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class);
  }
}
