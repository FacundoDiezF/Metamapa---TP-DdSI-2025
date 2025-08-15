package ar.utn.edu.frba.ddsi.models.entities.event;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Tag;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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
public class Event {

  @Setter
  private Long id;

  //-- Source
  @NonNull
  private final Source source;
  private final Long inSourceEventId;

  //-- Event Info
  private  String title;
  private  String description;
  private  Category category;
  private  Double latitude;
  private  Double longitude;
  private  LocalDateTime eventDate;

  //-- Funcionales
  private final LocalDateTime uploadDate;
  @Builder.Default public Set<Tag> tags = new HashSet<>();
  @Builder.Default private boolean deleted = false;


  public void update(EventInputDTO dto, Category category) {
    if (dto.getTitle() != null) this.title = dto.getTitle();
    if (dto.getDescription() != null) this.description = dto.getDescription();
    if (category != null) this.category = category;
    if (dto.getLatitude() != null) this.latitude = dto.getLatitude();
    if (dto.getLongitude() != null) this.longitude = dto.getLongitude();
    if (dto.getEventDate() != null) this.eventDate = dto.getEventDate();
  }

  public void markAsDeleted() {
    this.deleted = true;
    // Update state in the origin source
    source.notifyEventDeleted(this);
  }
}
