package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @ManyToOne
  @JoinColumn(name = "source_id", nullable = false)
  @Setter private Source source;

  //-- Description
  @Column(name = "title", nullable = false)
  private final String title;

  @Column(name = "description", nullable = false)
  private final String description;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  @Setter private Category category;


  @Column(name = "latitude", nullable = false)
  private final Double latitude;

  @Column(name = "longitude", nullable = false)
  private final Double longitude;


  @Column(name = "event_date", nullable = false)
  private final LocalDateTime eventDate;

  @Column(name = "upload_date", nullable = false)
  private final LocalDateTime uploadDate;

  @Builder.Default
  @Column(name = "deleted", nullable = false)
  private boolean deleted = false;

  public Origin getOrigin() {
    return this.source.getType();
  }

  public Long getSourceId() {
    return source.getId();
  }

  public void markAsDeleted() {
    this.deleted = true;
  }
}
