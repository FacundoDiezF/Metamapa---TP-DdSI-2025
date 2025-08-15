package ar.utn.edu.frba.ddsi.models.dtos.output.source;

import ar.utn.edu.frba.ddsi.models.entities.source.impl.SourceClient;
import lombok.Data;

@Data
public class SourceClientOutputDTO {
  Long id;
  String url;

  public static SourceClientOutputDTO from(SourceClient client) {
    SourceClientOutputDTO dto = new SourceClientOutputDTO();
    dto.setId(client.getId());
    dto.setUrl(client.getUrl());
    return dto;
  }
}