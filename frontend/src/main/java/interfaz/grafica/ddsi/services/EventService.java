package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.LocationDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import java.time.LocalDate;
import java.util.List;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class EventService {


  private final ServerAdapter server;

  public EventService(ServerAdapter server){
    this.server = server;
  }

  public PageDTO<EventDTO> getAll(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, int page) {
    try {
      return server.getAllEvents(category_in, province_in, eventDate_lt, eventDate_gt, source_in, page);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public List<EventDTO> getAllByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in) {
    //TODO TEST
    //!No está paginado
    try {
      return server.getAllEventsByCollection(handler);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }

  }

  public void postNew(EventOutputDTO newEvent) {
    //TODO que le mande el evento al agregador (Debemos crear el endpoint y que rediriga a la fuente dinamica)
  }
}
