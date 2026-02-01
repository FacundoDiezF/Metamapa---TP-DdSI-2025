package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MultimediaOutputDTO {
  private String path;
  private String name;
  private String mimeType;

  public static List<MultimediaOutputDTO> from(List<Multimedia> resources) {
    return resources.stream()
        .map(MultimediaOutputDTO::from)
        .toList();
  }

  private static MultimediaOutputDTO from(Multimedia resource) {
    String resourcePath = resource.getObjectStoragePath();
    return MultimediaOutputDTO.builder()
        .path(resourcePath)
        .name(resource.getName())
        .mimeType(resource.getMimeType())
        .build();
  }
}
