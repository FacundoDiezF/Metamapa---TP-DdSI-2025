package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class Aggregator implements ISubscriber {

  private final WebClient clientCallBack;
  private final Long sourceClientId;

  public Aggregator(String callbackUrl, Long sourceClientId) {
    this.clientCallBack = WebClient
        .builder()
        .baseUrl(callbackUrl)
        .build();
    this.sourceClientId = sourceClientId;
  }

  public static ISubscriber from(SubscriberInputDTO dto) {
    return new Aggregator(dto.getCallbackUrl(), dto.getSourceClientId());
  }

  public void notifyNewEvents(List<EventOutputDTO> events) {
    events.forEach(event -> event.setSourceClientId(sourceClientId));

    clientCallBack.post()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifySource(SourceOutputDTO source) {
    source.setSourceClientId(sourceClientId);
    clientCallBack.post()
        .uri("/sources")
        .bodyValue(source)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }
}
