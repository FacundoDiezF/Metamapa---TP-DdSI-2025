package ar.utn.edu.frba.ddsi.models.entities.collections;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionUpdateDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "collections")
public class Collection {

  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "handler")
  private String handler;

  @Setter
  @Column(name = "title")
  private String title;

  @Setter
  @Column(name = "description")
  private String description;

  @Builder.Default
  @Column(name = "updated_date", nullable = false)
  private LocalDateTime updatedDate = LocalDateTime.now(); //Only updated when events are added/removed

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "collection_criteria_id", nullable = false)
  @NonNull
  private final CollectionCriteria collectionCriteria;
  //TODO: Al crear una Collection, debería fijarme si la CollectionCriteria ya existe para no duplicarla
  //TODO: Al borrar la Collection, si solo ese usa la CollectionCriteria, tambien deberia borrarla

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "consensus")
  private ConsensusType consensusType;

  @ManyToMany
  @JoinTable(
      name = "collections_sources",
      joinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "handler"),
      inverseJoinColumns = @JoinColumn(name = "source_id", referencedColumnName = "id")
  )
  @NonNull
  private final Set<Source> sources;

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "collections_events",
      joinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "handler"),
      inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
  )
  private final Set<Event> events = new HashSet<>();

  public void refresh(LocalDateTime lastUpdate) {

    // Update the events, adding new ones and removing those that no longer satisfy the criteria.
    for (Source source : this.sources) {
      for (Event event : source.getEvents(lastUpdate)) {
        boolean wasAdded = false;
        boolean wasRemoved = false;
        if (this.collectionCriteria.isSatisfiedBy(event)) {
          wasAdded = events.add(event);
        } else {
          wasRemoved = events.remove(event);
        }

        if (wasAdded || wasRemoved) {
          updatedDate = LocalDateTime.now();
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

  public List<Long> getEventsIds() {
    return events.stream().map(Event::getId).toList();
  }

  private Set<Event> getMetamapaEvents() {
    //Pull and filter events from Metamapa sources (in parallel to improve performance).
    return this.sources.parallelStream()
        .filter(Source::isMetamapa)
        .flatMap(s -> s.fetchEvents().stream())
        .filter(this.collectionCriteria::isSatisfiedBy)
        .collect(Collectors.toSet());
  }

  public void update(CollectionUpdateDTO dto) {
    if (dto.getTitle() != null) this.title = dto.getTitle();
    if (dto.getDescription() != null) this.description = dto.getDescription();
  }

  public void remove(Source source) {
    sources.remove(source);
    boolean anyRemoved = events.removeIf(e -> e.getSource().equals(source));

    if (anyRemoved) {
      updatedDate = LocalDateTime.now();
    }
  }

  public void add(Source source) {
    sources.add(source);
    this.refresh(null);
  }
}