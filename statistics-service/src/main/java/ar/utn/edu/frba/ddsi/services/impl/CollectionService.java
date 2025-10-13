package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.CollectionIntputDTO;
import ar.utn.edu.frba.ddsi.models.entities.collection.Collection;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    List<CollectionIntputDTO> collectionDtos = AgregatorServiceWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/collections")
            .queryParam("fromUpdatedDate", lastUpdate)
            .build())
        .retrieve()
        .bodyToFlux(CollectionIntputDTO.class)
        .collectList()
        .block();

    if (collectionDtos == null || collectionDtos.isEmpty()) return;


    List<Collection> finalCollections = collectionDtos.stream()
        .map(this::buildCollection)
        .toList();

    collectionRepository.saveAll(finalCollections);
  }

  private Collection buildCollection(CollectionIntputDTO collectionDto) {

    List<Event> collectionEvents = eventRepository.findAllById(collectionDto.getEventIds());

    return Collection.builder()
        .handler(collectionDto.getHandler())
        .events(collectionEvents)
        .build();
  }
}
