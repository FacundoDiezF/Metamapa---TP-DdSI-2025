package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
public class SourceOutputDTO {
  Long inClientId;
  Origin type;

  String path;



    public static SourceOutputDTO from(Source source) {
        return SourceOutputDTO.builder()
                .inClientId(source.getId())
                .type(source.getType())
                .path(source.getPath())
                .build();
    }




}
