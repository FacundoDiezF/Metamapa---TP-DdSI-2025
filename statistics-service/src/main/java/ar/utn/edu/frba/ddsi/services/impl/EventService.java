package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.PageDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.Province;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;
  private final IProvinceRepository provinceRepository;
  private final ICategoryRepository categoryRepository;
  private final WebClient AgregatorServiceWebClient;


  @Autowired
  public EventService(IEventRepository eventRepository, IProvinceRepository provinceRepository, ICategoryRepository categoryRepository, @Value("${aggregation-service.url}") String aggregatorBaseUrl) {
    this.eventRepository = eventRepository;
    this.provinceRepository = provinceRepository;
    this.categoryRepository = categoryRepository;
    this.AgregatorServiceWebClient = WebClient.builder().baseUrl(aggregatorBaseUrl).build();
  }

  @Override
  public void fetchEvents(LocalDateTime lastUpdate) {
    fetchCategories();
    fetchProvinces();

    int size = 100;
    boolean lastPage = false;
    List<EventInputDTO> allEvents = new ArrayList<>();

    for (int page = 0; !lastPage; page++) {
      int currentPage = page;
      PageDTO<EventInputDTO> eventPage = AgregatorServiceWebClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/events")
              .queryParam("fromUpdatedDate", lastUpdate)
              .queryParam("page", currentPage)
              .queryParam("size", size)
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTO<EventInputDTO>>() {
          })
          .block();

      if (eventPage != null) {
        allEvents.addAll(eventPage.getContent());
      }

      if (eventPage == null) throw new RuntimeException("Failed to fetch categories from Aggregator Service");
        lastPage = eventPage.isLast();
      }

      if (!allEvents.isEmpty()) {
        List<Event> finalEvents = allEvents.stream()
              .map(this::buildEvent)
              .toList();

      eventRepository.saveAll(finalEvents);
    }
  }

  private Event buildEvent(EventInputDTO dto) {

    Category category = categoryRepository.findByName(dto.getCategory());

    Province province = provinceRepository.findByName(dto.getLocation().getProvince());

    return Event.builder()
        .id(dto.getId())
        .category(category)
        .province(province)
        .eventDate(dto.getEventDate())
        .build();
  }

  private void fetchCategories() {
    int size = 100;
    boolean lastPage = false;
    List<Category> allCategories = new ArrayList<>();

    for (int page = 0; !lastPage; page++) {
      int currentPage = page;
      PageDTO<Category> categoryPage = AgregatorServiceWebClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/categories")
              .queryParam("page", currentPage)
              .queryParam("size", size)
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTO<Category>>() {})
          .block();

      if (categoryPage != null) {
        allCategories.addAll(categoryPage.getContent());
      }

      if (categoryPage == null) throw new RuntimeException("Failed to fetch categories from Aggregator Service");
      lastPage = categoryPage.isLast();
    }

    if (!allCategories.isEmpty()) {
      categoryRepository.saveAll(allCategories);
    }
  }

  private void fetchProvinces() {//TODO TEST
    List<Province> provinces = AgregatorServiceWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/locations/provinces")
            .build())
        .retrieve()
        .bodyToFlux(Province.class) //? A lo mejor hay que hacer un DTO (me daba bronca que el DTO y la entidad sean iguales)
        .collectList()
        .block();

    if (provinces == null || provinces.isEmpty()) return;

    provinceRepository.saveAll(provinces);
  }
}
