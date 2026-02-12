package ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.Setter;

@Entity
@Table(name = "multimedia")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Multimedia { // Creo que no hace falta tener el id que tiene en dynamic
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "object_storage_path", nullable = false, unique = true)
  private String objectStoragePath;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String mimeType; //image/jpeg, image/png, video/mp4, etc.

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  @Setter
  private Event event;
}
