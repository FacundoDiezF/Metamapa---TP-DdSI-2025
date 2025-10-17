package ar.utn.edu.frba.ddsi.models.entities.adapters;

import ar.utn.edu.frba.ddsi.models.dtos.input.api.APILocationInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.api.LocationAPIInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.api.LocationAPIOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.api.LocationOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class LocationAPI {
  private final WebClient webClient;
  private final int threadCount;
  private final int batchSize;

  public LocationAPI(@Value("${geo-ref.url}") String url,
                     @Value("${thread.count}") int threadCount,
                     @Value("${batch.size}") int batchSize,
                     @Value("${response.byte.size}") int responseByteSize) {
    this.webClient = WebClient.builder()
        .baseUrl(url)
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> configurer
                .defaultCodecs()
                .maxInMemorySize(responseByteSize))
            .build())
        .build();
    this.threadCount = threadCount;
    this.batchSize = batchSize;
  }

  public List<Location> getLocationsByCoordinate(Set<Coordinate> coordinates) {
    // 1. Convert to List<LocationDTO>
    List<LocationOutputDTO> locations = LocationOutputDTO.from(coordinates); //?set?

    // 2. Split into chunks of 1000
    List<LocationAPIOutputDTO> requests = this.chunkRequests(locations);

    // 3. Execute requests
    List<APILocationInputDTO> allResults = this.executeRequests(requests);

    // 4. Map to List<Location>
    return Location.from(allResults);
  }

  private List<LocationAPIOutputDTO> chunkRequests(List<LocationOutputDTO> locations) {
    List<LocationAPIOutputDTO> requests = new ArrayList<>();

    //1. Iterate in steps of 1000
    for (int start = 0; start < locations.size(); start += this.batchSize) {

      //1.1 Determine end index, ensuring it doesn't exceed list size
      int end = Math.min(start + this.batchSize, locations.size());

      //1.2 Create sublist
      LocationAPIOutputDTO dto = new LocationAPIOutputDTO();
      dto.setLocations(locations.subList(start, end));

      //1.3 Add to requests
      requests.add(dto);
    }

    //2. Return all requests
    return requests;
  }

  private List<APILocationInputDTO> executeRequests(List<LocationAPIOutputDTO> requests){
    List<APILocationInputDTO> allResults = new ArrayList<>();

     //1. Create thread pool
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    try {
      List<Future<List<APILocationInputDTO>>> futures = new ArrayList<>();

      // 1.1. Submit tasks
      for (LocationAPIOutputDTO req : requests) {
        futures.add(executor.submit(() -> this.execute(req)));
      }

      // 1.2. Collect results
      for (Future<List<APILocationInputDTO>> future : futures) {
        try {
          allResults.addAll(future.get());
        } catch (Exception e) {
          // TODO Handle/log error as needed
          throw new RuntimeException("Error executing location API request", e);
        }
      }
    } finally {
      executor.shutdown();
    }

    // 2. Return all results
    return allResults;
  }

  private List<APILocationInputDTO> execute(LocationAPIOutputDTO req) {
    // 1. Call API
    LocationAPIInputDTO response = webClient.post()
        .uri("/ubicacion")
        .bodyValue(req)
        .retrieve()
        .bodyToMono(LocationAPIInputDTO.class)
        .block();

    // 2. Extract results
    if (response == null || response.getResults() == null){
      // TODO Manejar este error para que detecte cuando la respuesta no es la esperada, no solo null
      throw new RuntimeException("Error executing location API request, null response");
      //TODO devolver un buen error cuando por ejemplo esta caída la API
    }
    return response.getResults()
        .stream()
        .map(APILocationInputDTO::from)
        .toList();
  }
}
