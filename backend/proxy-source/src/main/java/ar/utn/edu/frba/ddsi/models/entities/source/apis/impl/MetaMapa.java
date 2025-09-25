package ar.utn.edu.frba.ddsi.models.entities.source.apis.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.apis.IAPI;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

public class MetaMapa implements IAPI {

  private final WebClient webClient;

  public MetaMapa(String baseUrl) {
    this.webClient = WebClient.builder()
                              .baseUrl(baseUrl)
                              .build();
  }

  @Override
  public List<EventOutputDTO> importEvents(LocalDateTime localDateTime) {
    return webClient.get()
                    .uri("/events")
                    .retrieve()
                    .bodyToFlux(EventInputDTO.class)
                    .collectList()
                    .block()
                    .stream() //TODO manejar caso null
                    .map(EventOutputDTO::from)
                    .toList();
  }
}