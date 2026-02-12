package ar.utn.edu.frba.ddsi.models.dtos.output;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class ImportResultDTO {
    private Long sourceId;
    private Integer eventsCount;

    public ImportResultDTO(Long sourceId, Integer eventsCount) {
        this.sourceId = sourceId;
        this.eventsCount = eventsCount;
    }

}