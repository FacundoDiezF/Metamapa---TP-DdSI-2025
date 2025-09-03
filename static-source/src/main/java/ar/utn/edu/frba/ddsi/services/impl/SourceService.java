package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.entities.source.SourceFactory;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

  private final ISourceRepository sourceRepository;
  private final IEventRepository eventRepository;
  private final ICategoryRepository categoryRepository;
  private final SourceFactory sourceFactory;
  private final Publisher publisher;

  public SourceService(
        ISourceRepository sourceRepository,
        IEventRepository eventRepository,
        ICategoryRepository categoryRepository,
        SourceFactory sourceFactory,
        Publisher publisher
    ) {
      this.sourceRepository = sourceRepository;
      this.eventRepository = eventRepository;
      this.categoryRepository = categoryRepository;
      this.sourceFactory = sourceFactory;
      this.publisher = publisher;
    }



  @Override
  public SourceOutputDTO create(SourceInputDTO sourceInput) {

    Source source = sourceFactory.createFrom(sourceInput);

    sourceRepository.save(source);

    SourceOutputDTO sourceOutput = SourceOutputDTO.from(source);

    publisher.notifyNewSource(sourceOutput);

    return sourceOutput;
  }

  @Override
  public List<SourceOutputDTO> getSources() {
    return sourceRepository.findAll()
        .stream()
        .map(SourceOutputDTO::from)
        .collect(Collectors.toList());
  }

  public void importSourceEvents(Long id) {
    Source source = sourceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + id));

    Set<Event> importedEvents = source.importEvents();

    importedEvents.forEach(event -> { //TODO: Implementar de una mejor manera
      String categoryName = event.getCategory().getName();

      Category category = categoryRepository.findByName(categoryName);

      if (category == null) { //TODO: Verificar el correcto funcionamiento (y persistencia) de las Category.
        category = categoryRepository.save(new Category(categoryName));
      }
      event.setCategory(category);
    });

    eventRepository.saveAll(importedEvents);

    List<EventOutputDTO> outputEvents = importedEvents
        .stream()
        .map(EventOutputDTO::from)
        .toList();

    publisher.notifyNewEvents(outputEvents);
  }

  @Override
  public List<EventOutputDTO> getAllEventsBySourceId(Long id) {

    sourceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + id));

    List<Event> events = eventRepository.findBySourceId(id);

    //TODO Return 404 if source not found

    return events
        .stream()
        .map(EventOutputDTO::from)
        .toList();
  }
}