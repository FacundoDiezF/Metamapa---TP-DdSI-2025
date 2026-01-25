package ar.utn.edu.frba.ddsi.mappers;

import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import org.springframework.stereotype.Component;

@Component
public class SourceMapper {

    public SourceOutputDTO transformarASourceOutPut(Source source) {
        if (source == null) return null;

        return SourceOutputDTO.builder()
                .inClientId(source.getId())
                .type(Origin.STATIC)
                .path(source.getPath())
                .build();
    }
}