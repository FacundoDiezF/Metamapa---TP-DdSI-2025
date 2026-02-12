package ar.utn.edu.frba.ddsi.models.dtos.input.event;

import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MultimediaInputDTO {
  private String resourcePath;
  private String name;
  private String mimeType;
}
