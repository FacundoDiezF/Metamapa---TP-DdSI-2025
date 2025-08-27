package ar.utn.edu.frba.ddsi.models.entities.observer;

import ar.utn.edu.frba.ddsi.models.dtos.input.SubscriberInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import java.util.List;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
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

  public void notifyNewEvent(List<Event> event) {
    // Convertir cada evento a EventOutputDTO y establecer el sourceClientId
    List<EventOutputDTO> outputEvents = event.stream()
        .map(EventOutputDTO::from)
        .peek(e -> e.setSourceClientId(sourceClientId))
        .toList();

    clientCallBack.post()
        .uri("/events")
        .bodyValue(outputEvents)
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifyUpdatedEvent(Event event) {
    EventOutputDTO outputEvent = EventOutputDTO.from(event);
    outputEvent.setSourceClientId(sourceClientId);
    clientCallBack.put()
        .uri("/events")
        .bodyValue(List.of(outputEvent)) // El agregador espera una lista
        .retrieve()
        .bodyToMono(Void.class)//? Hay respuesta
        .block();
  }

  public void notifySource(SourceOutputDTO dto) {
    dto.setSourceClientId(sourceClientId);
    clientCallBack.post()
        .uri("/sources")
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }
}
