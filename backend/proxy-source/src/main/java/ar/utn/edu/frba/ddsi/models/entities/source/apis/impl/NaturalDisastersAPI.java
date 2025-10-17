package ar.utn.edu.frba.ddsi.models.entities.source.apis.impl;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.apis.IAPI;
import ar.utn.edu.frba.ddsi.models.external.EventPage;
import ar.utn.edu.frba.ddsi.models.external.LoginRequest;
import ar.utn.edu.frba.ddsi.models.external.LoginResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaturalDisastersAPI implements IAPI {

  private final String accessToken;
  private final WebClient webClient;
  private final int threadCount;

  public NaturalDisastersAPI(
      @Value("${natural-disasters.api.base-url}") String baseUrl,
      @Value("${natural-disasters.api.credentials.email}") String email,
      @Value("${natural-disasters.api.credentials.password}") String password,
      @Value("${thread.count}") int threadCount) {

    this.webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build();
    this.accessToken = login(email, password);
    this.threadCount = threadCount;
  }

  private String login(String email, String password) {
    return webClient.post()
        .uri("/api/login")
        .bodyValue(new LoginRequest(email, password))
        .retrieve()
        .bodyToMono(LoginResponse.class)
        .map(LoginResponse::getAccessToken)
        .block();
  }


  @Override
  public List<EventOutputDTO> importEvents(LocalDateTime lastUpdate) {
    // Primero importamos la primera página para conocer last_page
    EventPage firstPage = this.importPage(1);
    List<Event> allEvents = new ArrayList<>(firstPage.getEvents(lastUpdate));
    Integer totalPages = firstPage.getLastPage();

    if (totalPages == null || totalPages <= 1) { //? Tiene sentido esto?
      return allEvents.stream().map(EventOutputDTO::from).toList();
    }

    // Creamos un pool de threads con un tamaño adecuado
    ExecutorService executor = Executors.newFixedThreadPool(Math.min(threadCount, totalPages - 1));

    try {
      // Creamos una lista de tareas para páginas 2 hasta totalPages
      List<Callable<List<Event>>> tasks = new ArrayList<>();
      for (int i = 2; i <= totalPages; i++) {
        final int pageNumber = i;
        tasks.add(() -> this.importPage(pageNumber).getEvents(lastUpdate));
      }

      // Ejecutamos las tareas en paralelo
      List<Future<List<Event>>> futures = executor.invokeAll(tasks);

      for (Future<List<Event>> future : futures) {
        allEvents.addAll(future.get()); // Esperamos y agregamos los eventos
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException("Error al importar eventos de múltiples páginas", e);
    } finally {
      executor.shutdown();
    }

    return allEvents.stream().map(EventOutputDTO::from).toList();
  }


  private EventPage importPage(int currentPage) {
    return webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/desastres")
            .queryParam("page", currentPage)
            .queryParam("per_page", 100)
            .build())
        .header("Authorization", "Bearer " + accessToken)
        .header("Accept", "application/json")
        .retrieve()
        .bodyToMono(EventPage.class)
        .block();
  }

}
