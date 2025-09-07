package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

  @Column(name = "source_id", nullable = false)
  @Builder.Default
  private final Long sourceId = 1L; // En dinamic siempre es 1

  @Column(name = "origin", nullable = false) //TODO revisar esta columba
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private final Origin origin = Origin.DYNAMIC;

  //-- Description
  @Setter
  @Column(name = "title", nullable = false)
  private String title;

  @Setter
  @Column(name = "description", nullable = false)
  private String description;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  //-- Ubicacion
  @Setter
  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Setter
  @Column(name = "longitude", nullable = false)
  private Double longitude;

  //-- Fechas
  @Setter
  @Column(name = "eventDate", nullable = false)
  private LocalDateTime eventDate;

  @Setter
  @Column(name = "uploadDate", nullable = false)
  private LocalDateTime uploadDate;

  @Setter
  @Column(name = "updateDate")
  private LocalDateTime updateDate;

  //From Dynamic
  @Column(name = "contributor", nullable = false)
  private final String contributor; //TODO: Esto deberia ser un usuario

  //-- Extras
  @Setter
  @Column(name = "deleted", nullable = false)
  @Builder.Default
  private boolean deleted = false;

  @Setter
  @Column(name = "accepted", nullable = false)
  @Builder.Default
  private boolean accepted = false;

  public void markAsAccepted() {
    this.accepted = true;
    this.uploadDate = LocalDateTime.now();
  }

  public void markAsDeleted() {
    this.deleted = true;
  }
}
