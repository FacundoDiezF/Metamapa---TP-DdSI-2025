package ar.utn.edu.frba.ddsi.models.entities.source.impl;

import ar.utn.edu.frba.ddsi.exceptions.SubscriptionException;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SubscribeOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.ISourceClientAdapter;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;


@Getter
public class SourceClient implements ISourceClientAdapter {

  @Setter
  private Long id;
  private final String url;
  private final WebClient webClient;

  public SourceClient(String url) {
    this.url = url;
    this.webClient = WebClient.builder().baseUrl(url).build();
  }

  public static SourceClient from(SourceClientInputDTO dto) {
    if (dto == null || dto.getUrl() == null || dto.getUrl().isEmpty()) {
      throw new IllegalArgumentException("SourceClient URL cannot be null or empty");
    }
    return new SourceClient(dto.getUrl());
  }

  @Override
  public void subscribe(String callbackUrl) {
    try {
      webClient.post()
          .uri("/subscribers")
          .bodyValue(new SubscribeOutputDTO(this.id, callbackUrl))
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      throw new SubscriptionException("Error in subscription: " + e.getMessage());
    }
  }

  @Override
  public List<EventInputDTO> fetchEventsBySource(Source source) {
    List<EventInputDTO> eventsDTOs =
        webClient.get()
            .uri("/sources/" + source.getInClientId() + "/events")
            .retrieve()
            .bodyToFlux(EventInputDTO.class)
            .collectList()
            .block();

    if (eventsDTOs == null) return List.of();

    return eventsDTOs;
  }

  @Override
  public void deleteEvent(Event event) {
    try {
      webClient.delete()
          .uri("/events/" + event.getInSourceEventId())
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error updating event in source origin: " + e.getMessage(), e);
    }
  }

}
