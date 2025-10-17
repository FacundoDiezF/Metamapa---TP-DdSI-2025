package ar.utn.edu.frba.ddsi.models.external;

import ar.utn.edu.frba.ddsi.models.dtos.input.ExternalDisasterDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class EventPage {
  @JsonProperty("data")
  private List<ExternalDisasterDTO> data;

  @JsonProperty("last_page")
  private Integer last_page;


  public List<Event> getEvents(LocalDateTime lastUpdate) {
    //TODO Manejar caso data null
    return data
        .stream()
        .map(Event::from)
        .filter(event -> lastUpdate == null || event.isOutdated(lastUpdate))
        .toList();
  }

  public Integer getLastPage() {
    return last_page;
  }
}
