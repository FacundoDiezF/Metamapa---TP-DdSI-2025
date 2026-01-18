package interfaz.grafica.ddsi.models.entitites.adapters;

import interfaz.grafica.ddsi.dtos.CategoryDTO;
import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.DeletionRequestDTO;
import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import interfaz.grafica.ddsi.dtos.SubmissionRequestDTO;
import interfaz.grafica.ddsi.dtos.auth.AuthResponseDTO;
import interfaz.grafica.ddsi.dtos.auth.LoginRequestDTO;
import interfaz.grafica.ddsi.dtos.auth.RegisterRequestDTO;
import interfaz.grafica.ddsi.dtos.auth.UserSummaryDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestCreationDTO;
import interfaz.grafica.ddsi.dtos.output.DeletionRequestEvaluateDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.dtos.output.SubmissionRequestEvaluationDTO;
import interfaz.grafica.ddsi.dtos.output.collection.CollectionOutputDTO;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.services.internal.WebApiCallerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;

@Component
public class ServerAdapter {

  private final WebClient webClient;

  private final WebClient statsClient; //TODO: Borrar y remplazar su uso cuando tengamos el gateway

  private final WebClient dynamicClient; //TODO: Borrar y remplazar su uso cuando tengamos el gateway

  private final WebClient apiGatewayClient;

  private final WebApiCallerService webApiCallerService;


  public ServerAdapter(@Value("${aggregator.url}") String url, @Value("${api.gateway.url}") String apiGatewayUrl, WebApiCallerService webApiCallerService) {
    //TODO: Borrar y remplazar su uso cuando tengamos el gateway
    this.webClient = WebClient.builder()
        .baseUrl(url)
        .build();

    //TODO: Borrar y remplazar su uso cuando tengamos el gateway
    this.statsClient = WebClient.builder()
        .baseUrl("http://localhost:8084")
        .build();

    //TODO: Borrar y remplazar su uso cuando tengamos el gateway
    this.dynamicClient = WebClient.builder()
        .baseUrl("http://localhost:8081")
        .build();

    this.apiGatewayClient = WebClient.builder()
        .baseUrl(apiGatewayUrl)
        .build();

    this.webApiCallerService = webApiCallerService;
  }


  //--------------- EVENTS ----------------//

  public void postNewEvent(EventOutputDTO newEvent) {
    webApiCallerService.post("/api/v1/events", newEvent, Void.class);
  } //* Funciona

  public PageDTO<EventDTO> getAllEvents(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size) {
    return webApiCallerService.get(uriBuilder -> {
          uriBuilder.path("/api/v1/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, page, size, uriBuilder);
        },
        new ParameterizedTypeReference<PageDTO<EventDTO>>() {
        });
  } //* Funciona

  public List<EventDTO> getAllEventsByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size) { //? Me llama la atencion que esto reciba una lista de EventDTO y no una PageDTO<EventDTO>
    return webApiCallerService.get(uriBuilder -> {
          uriBuilder.path("/api/v1/collections/" + handler + "/events");
          return getEventsUri(category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, page, size, uriBuilder);
        },
        new ParameterizedTypeReference<List<EventDTO>>() {
        });
  } //* Funciona

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
    return webApiCallerService.get(uriBuilder -> uriBuilder
            .path("/api/v1/collections")
            .queryParam("page", page)
            .queryParam("size", size)
            .build(),
        new ParameterizedTypeReference<PageDTO<CollectionDTO>>() {
        });
  } //* Funciona

  public CollectionDTO getCollection(String handler) {
    return webApiCallerService.get(uriBuilder -> uriBuilder
            .path("/api/v1/collections/{handler}")
            .build(handler),
        new ParameterizedTypeReference<CollectionDTO>() {
        });
  } //* Funciona

  public void postNewCollection(CollectionOutputDTO collectionDTO) {
    webApiCallerService.post("/api/v1/collections", collectionDTO, Void.class);
  } //TODO TEST

  public void updateCollection(String handler, CollectionOutputDTO collectionDTO) {
    webClient.put()
        .uri("/collections/{handler}", handler)
        .bodyValue(collectionDTO)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  public void deleteCollection(String handler) {
    webClient.delete()
        .uri("/collections/{handler}", handler)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  //--------------- SOURCES ----------------//
  public List<SourceDTO> getAllSources() {
    return webClient.get()
        .uri("/sources")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<SourceDTO>>() {
        })
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
  public void postNewDeletionRequest(DeletionRequestCreationDTO deletionRequestDTO) {
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

  //--------------- DELETION REQUESTS ----------------//
  public PageDTO<DeletionRequestDTO> getAllDeletionRequests(int page, int size) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/deletion-request")
            .queryParam("page", page)
            .queryParam("size", size)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<DeletionRequestDTO>>() {
        })
        .block();
  }

  public void evaluateDeletionRequest(Long eventId, DeletionRequestEvaluateDTO dto) {
    webClient.put()
        .uri(uriBuilder -> uriBuilder
            .path("/deletion-request/{eventId}")
            .build(eventId))
        .bodyValue(dto)
        .retrieve()
        .bodyToMono(Void.class)
        .block();
  }

  //--------------- DELETION REQUESTS ----------------//
  public PageDTO<SubmissionRequestDTO> getAllSubmissionRequest(int page, int size) {
    return dynamicClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/submissions")
            .queryParam("page", page)
            .queryParam("size", size)
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<PageDTO<SubmissionRequestDTO>>() {
        })
        .block();
  }

  public void evaluateSubmissionRequest(Long subId, SubmissionRequestEvaluationDTO dto) {
    webApiCallerService.put("api/v1/submissions/" + subId, dto, Void.class);
  }

  //--------------- AUTHENTICATION ----------------//
  public AuthResponseDTO login(LoginRequestDTO request) {

    if ("admin".equals(request.getUsername()) || "cont".equals(request.getUsername())) {
      return crearMockUser(request.getUsername());
    } //! Esto está mockeado full croto para que ustedes puedan probar el front sin que funcione el back, cuando esté el back, vuela

    try {
      return apiGatewayClient.post()
          .uri("/api/v1/auth/login")
          .bodyValue(request)
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)
          .block();
    } catch (WebClientResponseException e) {

      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        throw new BadCredentialsException("Invalid username or password.");
      }

      throw new BackendException(e);

    } catch (WebClientRequestException e) {
    throw new ConnectionException("Could not connect to the authentication server.");

  }catch (ResourceAccessException | IllegalStateException e) {
      throw new ConnectionException("Could not connect to the server.");
    }
  }

  public AuthResponseDTO register(RegisterRequestDTO request) {
    try {
    return apiGatewayClient.post()
        .uri("/api/v1/auth/register")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AuthResponseDTO.class)
        .block();
    } catch (WebClientResponseException e) {

      throw new BackendException(e);

    } catch (WebClientRequestException e) {
      throw new ConnectionException("Could not connect to the registration server.");

    }catch (ResourceAccessException | IllegalStateException e) {
      throw new ConnectionException("Could not connect to the server.");
    }
  }

  //!--------------- MOCK ----------------// cuando esté el back, vuela
  private AuthResponseDTO crearMockUser(String type) {
    AuthResponseDTO response = new AuthResponseDTO();
    response.setAccessToken("token-falso-" + type);
    response.setRefreshToken("refresh-falso-" + type);

    UserSummaryDTO user = new UserSummaryDTO();
    user.setUsername(type);
    user.setId(java.util.UUID.randomUUID().toString());

    if ("admin".equals(type)) {
      user.setRoles(java.util.List.of("ADMIN", "CONTRIBUTOR")); // El admin suele tener ambos
    } else {
      user.setRoles(java.util.List.of("CONTRIBUTOR"));
    }

    response.setUser(user);
    return response;
  }
}
