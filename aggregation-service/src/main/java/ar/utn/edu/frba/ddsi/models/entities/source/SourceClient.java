package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.exceptions.SubscriptionException;
import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SubscribeOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table(name = "source_clients")
public class SourceClient {

  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "url", nullable = false)
  private String url;

  @Transient
  private WebClient webClient;

  @PostLoad
  private void initWebClient() {
    if (this.url != null && this.webClient == null) {
      this.webClient = WebClient.builder().baseUrl(this.url).build();
    }
  }

  public static SourceClient from(SourceClientInputDTO dto) {
    if (dto == null || dto.getUrl() == null || dto.getUrl().isEmpty()) {
      throw new IllegalArgumentException("SourceClient URL cannot be null or empty");
    }
    SourceClient client = SourceClient.builder()
        .url(dto.getUrl())
        .build();

    client.initWebClient();

    return client;
  }

  //   http://localhost:8080/source-clients/:id

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

  public List<EventInputDTO> fetchEventsBySource(Source source) {
    List<EventInputDTO> eventsDTOs =
        webClient.get()
            .uri("/sources/" + source.getOriginSourceId() + "/events")
            .retrieve()
            .bodyToFlux(EventInputDTO.class)
            .collectList()
            .block();

    if (eventsDTOs == null) return List.of();

    return eventsDTOs;
  }

  public void deleteEvent(Event event) {
    try {
      webClient.delete()
          .uri("/events/" + event.getOriginEventId())
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error updating event in source origin: " + e.getMessage(), e);
    }
  }

}
