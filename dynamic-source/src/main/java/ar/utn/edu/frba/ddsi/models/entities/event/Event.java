package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

import javax.xml.transform.Source;

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

  @Column(name = "source_id", nullable = false)
  private Long sourceId;

  @Column(name = "origin", nullable = false) //TODO revisar esta columba
  private final Origin origin;

  //-- Description
  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id" , nullable = false)
  private Category category;

  //-- Ubicacion
  @Column(name = "latitude", nullable = false)
  private Double latitude;

    @Column(name = "longitude", nullable = false)
  private Double longitude;

  //-- Fechas
  @Column(name = "eventDate", nullable = false)
  private LocalDateTime eventDate;

    @Column(name = "uploadDate", nullable = false)
  private LocalDateTime uploadDate;

  //From Dynamic
  @Column(name = "contributor", nullable = false)
  private final String contributor; //TODO: Esto deberia ser un usuario

  //-- Extras
  @Setter
  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  @Setter
  @Column(name = "accepted", nullable = false)
  private boolean accepted;

  public static Event from(EventDTO dto) {
    return new Event(
        1L, //En la dinámica el sourceId es siempre el mismo puesto que no existen Sources. Esto permite respetar el formato del agregador
        dto.getTitle(),
        dto.getDescription(),
        new Category(dto.getCategory()), //TODO: Manejar Categorias
        dto.getLatitude(),
        dto.getLongitude(),
        dto.getEventDate(),
        Origin.DYNAMIC,
        dto.getContributor()
    );
  }

  public Event(Long sourceId,
               String title,
               String description,
               Category category,
               Double latitude,
               Double longitude,
               LocalDateTime eventDate,
               Origin origin,
               String contributor) {
    this.sourceId = sourceId;
    this.title = title;
    this.description = description;
    this.category = category;
    this.latitude = latitude;
    this.longitude = longitude;
    this.eventDate = eventDate;
    this.contributor = contributor;
    this.uploadDate = LocalDateTime.now();
    this.origin = origin;
    this.deleted = false;
  }

  public void update(EventDTO dto) {
    this.title = dto.getTitle();
    this.description = dto.getDescription();
    this.category = new Category(dto.getCategory()); //TODO: Manejar Categorias
    this.latitude = dto.getLatitude();
    this.longitude = dto.getLongitude();
    this.eventDate = dto.getEventDate();

    //Actualiza fecha de carga a la fecha de modificacion
    this.uploadDate = LocalDateTime.now();
  }

  public void markAsAccepted() {
    this.accepted = true;
    this.uploadDate = LocalDateTime.now();
  }
  public void markAsDeleted() {
    this.deleted = true;
  }
}
