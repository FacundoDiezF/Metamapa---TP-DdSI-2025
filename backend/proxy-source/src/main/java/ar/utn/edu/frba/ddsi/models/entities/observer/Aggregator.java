package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
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
  public Mono<Void> notifyUpdatedEvents(List<EventOutputDTO> events) {
    return clientCallBack.put()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class);
  }

  @Override
  public Mono<Void> notifySource(Source source) {
    SourceOutputDTO dto = SourceOutputDTO.from(source);
    return clientCallBack.post()
        .uri("/sources")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class);
  }
}
