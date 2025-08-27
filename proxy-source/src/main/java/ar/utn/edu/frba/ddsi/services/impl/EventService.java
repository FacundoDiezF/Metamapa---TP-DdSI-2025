package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService implements IEventService {

  ISourceRepository sourceRepository;

  Publisher publisher;

  public EventService(ISourceRepository sourceRepository, Publisher publisher) {
    this.sourceRepository = sourceRepository;
    this.publisher = publisher;
  }

  @Override
  public List<EventOutputDTO> getEvents(LocalDateTime lastUpdate) {
    return sourceRepository.findNonMetaMapa()
        .stream()
        //Obtengo todos los elementos de todas las fuentes
        .flatMap(source -> source.importEvents(lastUpdate).stream())
        .toList();
  }

  public void notifyEvents(LocalDateTime lastUpdate) {
    List<EventOutputDTO> events = this.getEvents(lastUpdate);

    for(EventOutputDTO event : events) {
      if(event.getUpdateDate().isEqual(event.getUploadDate())) { //TODO: Verificar si esta validación debería ser realizada dentro de la entidad Event (event.isNew())
        publisher.notifyNewEvents(events);
      } else {
        publisher.notifyUpdatedEvents(List.of(event));
      }
    }

  }

}
