package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
public class Event {

  //-- Identificador
  @Setter
  private Long id;
  private final Long sourceId;
  private final Origin origin;

  //-- Description
  private String title;
  private String description;
  private Category category;

  //-- Ubicacion
  private Double latitude;
  private Double longitude;

  //-- Fechas
  private LocalDateTime eventDate;
  private LocalDateTime uploadDate;

  //From Dynamic
  private final String contributor; //TODO: Esto deberia ser un usuario

  //-- Extras
  @Setter
  private boolean deleted;
  @Setter
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
