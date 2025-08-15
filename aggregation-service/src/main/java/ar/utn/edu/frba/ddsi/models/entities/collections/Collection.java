package ar.utn.edu.frba.ddsi.models.entities.collections;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class Collection {

  @Setter
  private String handler;

  private final String title;
  private final String description;

  @NonNull private final List<Source> sources;
  @NonNull private final CollectionCriteria collectionCriteria;

  @Builder.Default
  private final Set<Event> events = new HashSet<>();

  public void refresh(LocalDateTime lastUpdate) {

    // Update the events, adding new ones and removing those that no longer satisfy the criteria.
    for (Source source : this.sources) {
      for (Event event : source.getEvents(lastUpdate)) {
        if (this.collectionCriteria.isSatisfiedBy(event)) {
          events.add(event);
        } else {
          events.remove(event);
        }
      }
      // PD: No se filtra las MetaMapa ya que no deberian tener events en su lista.
    }

    // Remove deleted events
    this.events.removeIf(Event::isDeleted);
  }

  public Set<Event> getEvents() {
    Set<Event> allEvents = new HashSet<>(events);
    allEvents.addAll(getMetamapaEvents());
    return allEvents;
  }

  public Set<Event> getMetamapaEvents() {
    //Pull and filter events from Metamapa sources (in parallel to improve performance).
    return this.sources.parallelStream()
        .filter(Source::isMetamapa)
        .flatMap(s -> s.fetchEvents().stream())
        .filter(this.collectionCriteria::isSatisfiedBy)
        .collect(Collectors.toSet());
  }
}