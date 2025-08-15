package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Source {
  @Setter
  private Long id;

  //-- Source Client
  @NonNull
  private final ISourceClientAdapter sourceClient;
  private final Long inClientId;

  //-- Data
  private final Origin type;

  @Builder.Default
  private final List<Event> events = new ArrayList<>();

  public List<Event> getEvents(LocalDateTime lastUpdate) {
    if (lastUpdate == null) {
      return events;
    }
    return events.stream().filter(e ->
            e.getUploadDate().isAfter(lastUpdate) && !e.isDeleted())
        .toList();
  }

  public void addEvent(Event event) {
    events.add(event);
  }

  public void notifyEventDeleted(Event event) {
    if (this.isNotifiable()) {
      sourceClient.deleteEvent(event);
    }
  }

  public List<Event> fetchEvents() {
    // This method only applicable for Metamapa sources.
    if (!this.isMetamapa()) {
      throw new UnsupportedOperationException("This method should only be called for Metamapa sources");
    }

    return this.sourceClient.fetchEventsBySource(this)
        .stream()
        .map(dto -> {
          //Create “fake” event
          return Event.builder()
              .title(dto.getTitle())
              .description(dto.getDescription())
              .category(new Category(dto.getCategory()))
              .latitude(dto.getLatitude())
              .longitude(dto.getLongitude())
              .eventDate(dto.getEventDate())
              .uploadDate(dto.getUploadDate())
              .source(this)
              .inSourceEventId(dto.getId())
              .build();
        }).toList();
  }

  private boolean isNotifiable() {
    return type != Origin.PROXY && type != Origin.METAMAPA;
  }

  public boolean isMetamapa() {
    return type == Origin.METAMAPA;
  }
}
