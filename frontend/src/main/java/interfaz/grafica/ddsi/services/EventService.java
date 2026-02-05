package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class EventService {


  private final ServerAdapter server;

  public EventService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<EventDTO> getAll(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, int page, int size) {
    return server.getAllEvents(category_in, province_in, eventDate_lt, eventDate_gt, source_in, false, page, size); // La navegacion curada siempre es false 
  }

  public List<EventDTO> getAllByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in, boolean curatedNavigation, int page, int size) {
    // TODO No está paginado
    return server.getAllEventsByCollection(handler, category_in, province_in, eventDate_lt, eventDate_gt, source_in, curatedNavigation, page, size); 

  }

  public List<EventDTO> getAllByUser() {
    // TODO No está paginado
    return server.getAllEventsByUser();

  }

  public void postNew(EventOutputDTO newEvent, List<MultipartFile> files) {
    server.postNewEvent(newEvent, files);
  }
}
