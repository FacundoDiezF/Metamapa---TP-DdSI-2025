package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

  private final IEventRepository eventRepository;

  @Autowired
  public SourceService(IEventRepository eventRepository){
    this.eventRepository = eventRepository;
  }

  @Override
  public List<SourceOutputDTO> getSources() {

    //Dynamic has no sources, but we emulate one for traceability purposes between microservices
    SourceOutputDTO dto = new SourceOutputDTO();
    dto.setInClientId(1L);
    dto.setType(Origin.DYNAMIC);

    return List.of(dto);
  }

  @Override
  public List<EventOutputDTO> getAllEventsBySourceId(Long id) {

    //Dynamic has no sources, but we emulate one for traceability purposes between microservices
    if(id != 1L){
      throw new NotFoundException("Source with id " + id + " not found");
    }

    List<Event> events = eventRepository.findBySourceId(id);

    return events
          .stream()
          .map(EventOutputDTO::from)
          .toList();
  }
}