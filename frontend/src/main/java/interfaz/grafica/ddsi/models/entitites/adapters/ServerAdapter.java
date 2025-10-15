package interfaz.grafica.ddsi.models.entitites.adapters;
import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDate;
import java.util.List;

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
          if (category_in != null && !category_in.isEmpty()) {
            category_in.forEach(cat -> uriBuilder.queryParam("category_in", cat));
          }
          if (province_in != null && !province_in.isEmpty()) {
            province_in.forEach(prov -> uriBuilder.queryParam("province_in", prov));
          }
          if (eventDate_lt != null) {
            uriBuilder.queryParam("eventDate_lt", eventDate_lt.atStartOfDay());
          }
          if (eventDate_gt != null) {
            uriBuilder.queryParam("eventDate_gt", eventDate_gt.atStartOfDay());
          }
          if (source_in != null && !source_in.isEmpty()) {
            source_in.forEach(src -> uriBuilder.queryParam("source_in", src));
          }
          uriBuilder.queryParam("page", page);
          return uriBuilder.build();
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<EventDTO>>() {})
        .block();
  }

  public List<EventDTO> getAllEventsByCollection(String handler) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("collections/"+ handler + "/events")
            //.queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToFlux(EventDTO.class)
        .collectList()
        .block();
  }

  public PageDTO<CollectionDTO> getAllCollections(int page) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/collections")
            .queryParam("page", page)
            //.queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<CollectionDTO>>() {})
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
        .bodyToMono(new ParameterizedTypeReference<PageDTO<CategoryDTO>>() {})
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
        .bodyToMono(new ParameterizedTypeReference<PageDTO<ProvinceDTO>>() {})
        .block();
  }
}
