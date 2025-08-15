package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.event.EventInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface IEventService {
  List<EventOutputDTO> getEvents(
      String category,
      LocalDateTime untilUploadDate,
      LocalDateTime fromUploadDate,
      LocalDateTime untilEventDate,
      LocalDateTime fromEventDate);

  void create(EventInputDTO dto);

  void createAll(List<EventInputDTO> dtos);

  void update(Long id, EventInputDTO dto);
}
