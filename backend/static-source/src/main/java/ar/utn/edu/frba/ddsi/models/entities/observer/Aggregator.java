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
  public Mono<Void> notifyNewEvents(List<EventOutputDTO> events) {
    return clientCallBack.post()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<Void> notifySource(SourceOutputDTO source) {
    return clientCallBack.post()
        .uri("/sources")
        .bodyValue(source)
        .retrieve()
        .bodyToMono(Void.class);
  }
}
