package interfaz.grafica.ddsi.models.entitites.adapters;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Component
public class ServerAdapter {

  private final WebClient webClient;

  public ServerAdapter(@Value("${aggregator.url}") String url) {
    this.webClient = WebClient.builder()
        .baseUrl(url)
        .build();
  }

  public PageDTO<EventDTO> getAllEvents(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, int page) {
    return webClient.get()
        .uri(uriBuilder -> {
          uriBuilder.path("/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page, uriBuilder);
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<EventDTO>>() {
        })
        .block();
  }

  public List<EventDTO> getAllEventsByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, int page) {
    return webClient.get()
        .uri(uriBuilder -> {
          uriBuilder.path("collections/" + handler + "/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page, uriBuilder);
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<EventDTO>>() {
        })
        .block();
  }

  private URI getEventsUri(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, int page, UriBuilder uriBuilder) {
    if (category_in != null && !category_in.isEmpty()) {
      uriBuilder.queryParam("category_in", category_in.toArray());
    }
    if (province_in != null && !province_in.isEmpty()) {
      uriBuilder.queryParam("province_in", province_in.toArray());
    }
    if (eventDate_lt != null) {
      uriBuilder.queryParam("eventDate_lt", eventDate_lt.atStartOfDay());
    }
    if (eventDate_gt != null) {
      uriBuilder.queryParam("eventDate_gt", eventDate_gt.atStartOfDay());
    }
    if (source_in != null && !source_in.isEmpty()) {
      uriBuilder.queryParam("source_in", source_in.toArray());
    }
    uriBuilder.queryParam("page", page);
    return uriBuilder.build();
  }

  public PageDTO<CollectionDTO> getAllCollections(int page) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/collections")
            .queryParam("page", page)
            //.queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<CollectionDTO>>() {
        })
        .block();
  }

  public CollectionDTO getCollection(String handler) {
    return webClient.get()
        .uri("/collections/{handler}", handler)
        .retrieve()
        .bodyToMono(CollectionDTO.class)
        .block();
  }

  public PageDTO<CategoryDTO> getAllCategories(int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/categories")
            .queryParam("page", page)
            .queryParam("size", size)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<CategoryDTO>>() {
        })
        .block();
  }

  public PageDTO<ProvinceDTO> getAllProvinces(int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/locations/provinces")
            .queryParam("page", page)
            .queryParam("size", size)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<ProvinceDTO>>() {
        })
        .block();
  }
}
