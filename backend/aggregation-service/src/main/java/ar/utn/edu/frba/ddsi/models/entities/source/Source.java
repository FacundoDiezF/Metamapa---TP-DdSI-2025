package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "sources")
public class Source {
  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //-- Source Client
  @NonNull
  @ManyToOne
  @JoinColumn(name = "source_client_id", nullable = false)
  private final SourceClient sourceClient;

  @Column(name = "origin_source_id", nullable = false)
  private final Long originSourceId;// Source ID in the original source module

  //-- Data
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private final Origin type;

  @Builder.Default
  @OneToMany(mappedBy = "source")
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
              .location(new Location(Coordinate
                                    .builder()
                                    .latitude(dto.getLatitude())
                                    .longitude(dto.getLongitude())
                                    .build(),
                                    null, null, null)) //TODO: IMPORTANTE VER COMO SE VAN A CUMUNICAR LAS LOCATION ACTUALMENTE ESTA HARDCODEADO
              .eventDate(dto.getEventDate())
              .uploadDate(dto.getUploadDate())
              .source(this)
              .originEventId(dto.getId())
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
