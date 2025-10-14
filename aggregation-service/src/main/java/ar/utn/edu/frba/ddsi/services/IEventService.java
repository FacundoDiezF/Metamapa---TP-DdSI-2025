package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Origin;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEventService {
  Page<EventOutputDTO> getEvents(
      List<Long> category_in,
      List<Long> province_in,
      List<Origin> source_in,
      LocalDateTime uploadDate_lt,
      LocalDateTime uploadDate_gt,
      LocalDateTime eventDate_lt,
      LocalDateTime eventDate_gt,
      LocalDateTime fromUpdatedDate,
      Pageable pageable
  );

  void createAll(List<EventInputDTO> dtos);

  void updateAll(List<EventInputDTO> dtos);
}
