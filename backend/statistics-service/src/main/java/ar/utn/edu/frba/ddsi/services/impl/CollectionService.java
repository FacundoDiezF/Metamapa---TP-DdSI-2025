package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.CollectionIntputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.PageDTO;
import ar.utn.edu.frba.ddsi.models.entities.collection.Collection;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CollectionService implements ICollectionService {

  private final IEventRepository eventRepository;
  private final ICollectionRepository collectionRepository;
  private final WebClient AgregatorServiceWebClient;


  @Autowired
  public CollectionService(IEventRepository eventRepository, ICollectionRepository collectionRepository, @Value("${aggregation-service.url}") String aggregatorBaseUrl) {
    this.eventRepository = eventRepository;
    this.collectionRepository = collectionRepository;
    this.AgregatorServiceWebClient = WebClient.builder().baseUrl(aggregatorBaseUrl).build();
  }

  @Override
  public void fetchCollections(LocalDateTime lastUpdate) {
    // Fetch new events from Aggregator Service
    int size = 100;
    boolean lastPage = false;
    List<CollectionIntputDTO> allCollections = new ArrayList<>();

    for (int page = 0; !lastPage; page++) {
      int currentPage = page;
      PageDTO<CollectionIntputDTO> collectionPage = AgregatorServiceWebClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/collections")
              .queryParam("fromUpdatedDate", lastUpdate)
              .queryParam("page", currentPage)
              .queryParam("size", size)
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTO<CollectionIntputDTO>>() {})
          .block();

      if (collectionPage != null) {
        allCollections.addAll(collectionPage.getContent());
      }

      if (collectionPage == null) throw new RuntimeException("Failed to fetch collections from Aggregator Service");
      lastPage = collectionPage.isLast();
    }

    if (!allCollections.isEmpty()) {
      List<Collection> finalCollections = allCollections.stream()
      .map(this::buildCollection)
      .toList();

      collectionRepository.saveAll(finalCollections);
    }
  }



  private Collection buildCollection(CollectionIntputDTO collectionDto) {

    List<Event> collectionEvents = eventRepository.findAllById(collectionDto.getEventIds());

    return Collection.builder()
        .handler(collectionDto.getHandler())
        .events(collectionEvents)
        .build();
  }
}
