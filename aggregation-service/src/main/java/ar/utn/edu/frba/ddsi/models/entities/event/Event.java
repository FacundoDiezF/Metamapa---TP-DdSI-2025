package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  @ManyToOne
  @JoinColumn(name = "source_id", nullable = false)
  private final Source source;

  @Column(name = "origin_event_id", nullable = false)
  private final Long originEventId;// Event ID in the original source module

  //-- Event Info
  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "event_date", nullable = false)
  private LocalDateTime eventDate;

  //-- Funcionales
  @Column(name = "upload_date", nullable = false)
  private final LocalDateTime uploadDate;

  @Builder.Default
  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  //@ManyToMany
  //@Builder.Default public Set<Tag> tags = new HashSet<>();

  @ElementCollection
  @CollectionTable(name = "event_passed_consensus", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
  @Column(name = "consensus_type")
  @Builder.Default
  @Setter
  private List<ConsensusType> passedConsensus = new ArrayList<>();


  public void update(EventInputDTO dto, Category category) {
    if (dto.getTitle() != null) this.title = dto.getTitle();
    if (dto.getDescription() != null) this.description = dto.getDescription();
    if (category != null) this.category = category;
    if (dto.getLatitude() != null) this.latitude = dto.getLatitude();
    if (dto.getLongitude() != null) this.longitude = dto.getLongitude();
    if (dto.getEventDate() != null) this.eventDate = dto.getEventDate();
  }

  public boolean meetsAlgorithmConsensus(ConsensusType consensusType) {
    if (consensusType == null) return true;

    return passedConsensus.contains(consensusType);
  }

  public boolean isEquivalent(Event other) {
    if (other == null) return false;
    return Objects.equals(this.title, other.title)
        && Objects.equals(this.description, other.description)
        //&& Objects.equals(this.category, other.category) //TODO: Cuando tengamos el repo lo descomentamos
        && Objects.equals(this.latitude, other.latitude)
        && Objects.equals(this.longitude, other.longitude)
        && Objects.equals(this.eventDate, other.eventDate);
  }

  public void markAsDeleted() {
    this.deleted = true;
    // Update state in the origin source
    source.notifyEventDeleted(this);
  }
}
