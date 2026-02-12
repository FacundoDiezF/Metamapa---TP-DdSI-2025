package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import ar.utn.edu.frba.ddsi.models.entities.strategies.UrlGenerationStrategy;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MultimediaOutputDTO {
  private String url;
  private String resourcePath;
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
        .resourcePath(resourcePath)
        .name(resource.getName())
        .mimeType(resource.getMimeType())
        .build();
  }

  public static List<MultimediaOutputDTO> from(List<Multimedia> resources, UrlGenerationStrategy urlGenerationStrategy) {
    return resources.stream()
        .map(resource -> MultimediaOutputDTO.from(resource, urlGenerationStrategy))
        .toList();
  }

  private static MultimediaOutputDTO from(Multimedia resource, UrlGenerationStrategy urlGenerationStrategy) {
    String resourcePath = resource.getObjectStoragePath();
    return MultimediaOutputDTO.builder()
        .url(urlGenerationStrategy.generateUrl(resourcePath))
        .resourcePath(resourcePath)
        .name(resource.getName())
        .mimeType(resource.getMimeType())
        .build();
  }
}
