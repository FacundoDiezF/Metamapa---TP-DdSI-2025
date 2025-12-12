package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.entities.source.SourceFactory;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import ar.utn.edu.frba.ddsi.services.ISourceService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SourceService implements ISourceService {

    @Value("${static-source.storage.dir}")
    private String storageDir;

  private final ISourceRepository sourceRepository;
  private final IEventRepository eventRepository;
  private final IEventService eventService;

  private final SourceFactory sourceFactory;
  private final Publisher publisher;

  @Autowired
  public SourceService(ISourceRepository sourceRepository, IEventService eventService, IEventRepository eventRepository, SourceFactory sourceFactory, Publisher publisher) {
    this.sourceRepository = sourceRepository;
    this.eventService = eventService;
    this.eventRepository = eventRepository;
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
  public SourceOutputDTO createFromCsvFile(MultipartFile csvfile) throws IOException {

        if (csvfile.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV está vacío");
        }

      String userHome = System.getProperty("user.home");

      Path uploadsDir = Paths.get(userHome, storageDir);
      Files.createDirectories(uploadsDir);

      Path destino = uploadsDir.resolve(csvfile.getOriginalFilename());

      Files.copy(csvfile.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);


      SourceInputDTO dto = SourceInputDTO.builder()
                .fileName(destino.toString())
                .importStrategy("CSV")
                .imported(false)
                .build();

      System.out.println("Guardando CSV en: " + uploadsDir.toAbsolutePath());
      return this.create(dto);
  }

  @Override
  public List<SourceOutputDTO> getSources() {
    return sourceRepository.findAll().stream().map(SourceOutputDTO::from).collect(Collectors.toList());
  }

  public void importSourceEvents(Long id) { //! Actualmente permite importar cuantas veces quieras la misma fuente, generando eventos duplicados
    Source source = sourceRepository.findById(id).orElseThrow(() -> new NotFoundException("Source not found - ID: " + id));

    //import events (Create and save in repository)
    List<EventOutputDTO> importedEvents = source.importEvents().stream().map(eventService::createEvent).toList();

    publisher.notifyNewEvents(importedEvents);
  }

  @Override
  public List<EventOutputDTO> getAllEventsBySourceId(Long id) {

    sourceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + id));

    List<Event> events = eventRepository.findBySourceId(id);

    return events.stream().map(EventOutputDTO::from).toList();
  }


}