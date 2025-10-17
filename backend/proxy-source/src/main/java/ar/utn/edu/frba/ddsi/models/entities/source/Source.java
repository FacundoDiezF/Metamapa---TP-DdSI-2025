package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.apis.IAPI;
import ar.utn.edu.frba.ddsi.models.entities.source.apis.impl.MetaMapa;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Source {

  @Getter
  @Setter
  private Long id;
  @Getter
  @Setter
  private Origin type;

  private final IAPI api;

  public Source(IAPI api, Origin type) {
    this.api = api;
    this.type = type;
  }

  public List<EventOutputDTO>  importEvents(LocalDateTime lastUpdate) {
    List<EventOutputDTO> events = this.api.importEvents(lastUpdate);

    events.forEach(event -> event.setSourceId(this.id));

    return events;
  }

  public static Source from(SourceInputDTO dto) {
    return new Source(new MetaMapa(dto.getBaseUrl()), dto.getType());
  }
}

