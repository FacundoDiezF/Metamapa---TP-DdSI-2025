package interfaz.grafica.ddsi.models.entitites.adapters;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestDTO;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

@Component
public class ServerAdapter {

  private final WebClient webClient;

  private final WebClient statsClient; //TODO: Borrar y remplazar su uso cuando tengamos el gateway

  public ServerAdapter(@Value("${aggregator.url}") String url) {
    this.webClient = WebClient.builder()
        .baseUrl(url)
        .build();

    //TODO: Borrar y remplazar su uso cuando tengamos el gateway
    this.statsClient = WebClient.builder()
        .baseUrl("http://localhost:8084")
        .build();
  }


  //--------------- EVENTS ----------------//

  public PageDTO<EventDTO> getAllEvents(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> {
          uriBuilder.path("/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, page, size, uriBuilder);
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<EventDTO>>() {
        })
        .block();
  }

  public List<EventDTO> getAllEventsByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> {
          uriBuilder.path("collections/" + handler + "/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, page, size, uriBuilder);
        })
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<EventDTO>>() {
        })
        .block();
  }

  private URI getEventsUri(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size, UriBuilder uriBuilder) {
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
    if (curatedNavigation) {
      uriBuilder.queryParam("curatedNavigation", true);
    }
    uriBuilder.queryParam("page", page);
    uriBuilder.queryParam("size", size);
    return uriBuilder.build();
  }


  //--------------- COLLECTIONS ----------------//
  public PageDTO<CollectionDTO> getAllCollections(int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/collections")
            .queryParam("page", page)
            .queryParam("size", size)
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


  //--------------- CATEGORIES ----------------//
  public PageDTO<CategoryDTO> getAllCategories(int page, int size, List<Long> categoryIds) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/categories")
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("category_in", categoryIds)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<CategoryDTO>>() {
        })
        .block();
  }

  //Sobrecarga sin filtro de IDs
  public PageDTO<CategoryDTO> getAllCategories(int page, int size) {
    return getAllCategories(page, size, null);
  }

  public List<CategoryDTO> getAllCategoriesByIds(List<Long> categoriesIds) {

    if (categoriesIds == null || categoriesIds.isEmpty()) {
      return new ArrayList<>();
    }

    return getAllCategories(0, categoriesIds.size(), categoriesIds).getContent();
  }

  public CategoryDTO getCategoryById(Long idCategory) {

    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/categories/{idCategory}")
            .build(idCategory))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<CategoryDTO>() {
        })
        .block();
  }

  //--------------- PROVINCES ----------------//
  public PageDTO<ProvinceDTO> getAllProvinces(int page, int size, List<Long> provinceIds) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/locations/provinces")
            .queryParam("page", page)
            .queryParam("size", size)
            .queryParam("province_in", provinceIds)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<ProvinceDTO>>() {
        })
        .block();
  }

  //Sobrecarga sin filtro de IDs
  public PageDTO<ProvinceDTO> getAllProvinces(int page, int size) {
    return getAllProvinces(page, size, null);
  }

  public List<ProvinceDTO> getProvincesByIds(List<Long> provinceIds) {

    if (provinceIds == null || provinceIds.isEmpty()) {
      return new ArrayList<>();
    }

    return getAllProvinces(0, provinceIds.size(), provinceIds).getContent();
  }

  //--------------- DELETION REQUESTS ----------------//
  public void postNewDeletionRequest(DeletionRequestDTO deletionRequestDTO) {
    webClient.post()
        .uri("/deletion-request")
        .bodyValue(deletionRequestDTO)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  //--------------- STATISTICS ----------------//
  public List<StatisticsDTO> getTopCategories() {
    return statsClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/statistics/categories/top")
            .queryParam("count", 4)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StatisticsDTO>>() {
        })
        .block();
  }

  public List<StatisticsDTO> getTopProvincesByCategory(Long idCategory) {
    return statsClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/statistics/category/{idCategory}/provinces/top")
            .queryParam("count", 4)
            .build(idCategory))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StatisticsDTO>>() {
        })
        .block();
  }

  public List<StatisticsDTO> getBuisiestHoursByCategory(Long idCategory) {
    return statsClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/statistics/category/{idCategory}/busiest-hours/top")
            .queryParam("count", 4)
            .build(idCategory))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<StatisticsDTO>>() {
        })
        .block();
  }
}
