package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
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
import java.util.Objects;
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
@Table(name = "events")
public class Event {

  @Setter
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //-- Source
  @NonNull
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "source_id", nullable = false)
  private final Source source;

  @Column(name = "origin_event_id", nullable = false)
  private final Long originEventId;// Event ID in the original source module

  //-- Event Info
  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false, columnDefinition = "TEXT")
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Embedded
  @Setter
  @Getter
  private Location location;

  @Column(name = "event_date", nullable = false)
  private LocalDateTime eventDate; //TODO: Debe ser LocalDate

  //-- Funcionales
  @Column(name = "upload_date", nullable = false)
  private final LocalDateTime uploadDate;

  @Builder.Default
  @Column(name = "updated_date", nullable = false)
  private LocalDateTime updatedDate = LocalDateTime.now();

  @Builder.Default
  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "event_passed_consensus", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
  @Column(name = "consensus_type")
  @Builder.Default
  private List<ConsensusType> passedConsensus = new ArrayList<>();

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Multimedia> resources = new ArrayList<>();

  public void update(EventInputDTO dto, Category category) { //TODO: Hacer que actualice las multimedia. Ahora mismo solo se actualizan cuando se crea el evento

    this.updatedDate = LocalDateTime.now();

    if (dto.getTitle() != null && !Objects.equals(dto.getTitle(), this.title)) {
      this.title = dto.getTitle();
    }

    if (dto.getDescription() != null && !Objects.equals(dto.getDescription(), this.description)) {
      this.description = dto.getDescription();
    }

    if (category != null && !Objects.equals(category, this.category)) {
      this.category = category;
    }

    if (dto.getEventDate() != null && !Objects.equals(dto.getEventDate(), this.eventDate)) {
      this.eventDate = dto.getEventDate();
    }
  }

  public boolean isEquivalent(Event that) {
    if (that == null) return false;
    return Objects.equals(this.title, that.title)
        && Objects.equals(this.description, that.description)
        //&& Objects.equals(this.category, that.category) //TODO: Cuando tengamos el repo lo descomentamos
        && Objects.equals(this.location.getCoordinate(), that.location.getCoordinate())
        && Objects.equals(this.eventDate, that.eventDate);
  }

  public void markAsDeleted() {
    this.deleted = true;
    // Update state in the origin source
    source.notifyEventDeleted(this);
  }

  public void setPassedConsensus(List<ConsensusType> passedConsensus) {
    //For jpa tracking
    this.passedConsensus.clear();
    this.passedConsensus.addAll(passedConsensus);
  }

  public void addResources(Iterable<Multimedia> resources) {
    resources.forEach(this::addResource);
  }

  private void addResource(Multimedia resource) {
    resources.add(resource);
    resource.setEvent(this);
  }
}
