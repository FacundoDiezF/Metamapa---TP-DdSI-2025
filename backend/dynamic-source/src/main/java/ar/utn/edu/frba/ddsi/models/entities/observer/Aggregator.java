package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

public class Aggregator implements ISubscriber {

  private final WebClient clientCallBack;
  private final Long sourceClientId; //TODO: No se va a usar mas el id va a estaral en la url de callback

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

  public void notifyNewEvent(List<EventOutputDTO> events) {
    events.forEach(e -> e.setSourceClientId(sourceClientId)); //TODO: No se va a usar mas el id va a estaral en la url de callback

    clientCallBack.post()
        .uri("/events")
        .bodyValue(events)
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifyUpdatedEvent(EventOutputDTO outputEvent) {
    outputEvent.setSourceClientId(sourceClientId); //TODO: No se va a usar mas el id va a estaral en la url de callback

    clientCallBack.put()
        .uri("/events")
        .bodyValue(List.of(outputEvent)) // El agregador espera una lista
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifySource(SourceOutputDTO dto) {
    dto.setSourceClientId(sourceClientId); //TODO: No se va a usar mas el id va a estaral en la url de callback

    clientCallBack.post()
        .uri("/sources")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }
}
