package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.exceptions.UnauthorizedException;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.edition.EventEditionRequest;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.multimedia.Multimedia;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.repositories.IEditionRequestRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISubmissionRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IEventService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ar.utn.edu.frba.ddsi.services.storage.IStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventService implements IEventService {

  private final IEventRepository eventRepository;

  private final ISubmissionRepository submissionRepository;

  private final ICategoryService categoryService;

  private final IEditionRequestRepository editionRequestRepository;

  private final IStorageService storageService;

  @Value("${app.event.allowed-mime-types}")
  private List<String> allowedMimeTypes;

  @Value("${app.event.max-files}")
  private int maxAmountOfFiles;

  @Autowired
  public EventService(IEventRepository eventRepository, ISubmissionRepository submissionRepository, ICategoryService categoryService, IEditionRequestRepository editionRequestRepository, IStorageService storageService) {
    this.eventRepository = eventRepository;
    this.submissionRepository = submissionRepository;
    this.categoryService = categoryService;
    this.editionRequestRepository = editionRequestRepository;
    this.storageService = storageService;
  }

  @PostConstruct
  private void validateConfiguration() {
    for (String type : allowedMimeTypes) {
      try {
        // Esto chequea que el string tenga formato "tipo/subtipo"
        MimeTypeUtils.parseMimeType(type);
      } catch (InvalidMimeTypeException e) {
        throw new IllegalStateException("CONFIG ERROR: '" + type + "' is NOT a valid MIME type.");
      }
    }
  }

  @Override
  public List<EventOutputDTO> getAllEvents() {
    return eventRepository.findByAccepted().stream().map(EventOutputDTO::from).toList();
  }

  @Override
  public List<EventOutputDTO> getEventsByUserId(String userId) {
    return eventRepository.findByCreatorId(userId).stream().map(EventOutputDTO::from).toList();
  }

  @Transactional
  @Override
  public EventOutputDTO save(EventDTO dto, String userId, List<MultipartFile> multimedia) {

    if (multimedia != null && multimedia.size() > maxAmountOfFiles) {
      throw new IllegalArgumentException("Maximum number of files exceeded. Max allowed: " + maxAmountOfFiles);
    }

    validateFiles(multimedia);

    Category category = categoryService.findOrCreate(dto.getCategory());

    List<String> uploadedFilesPaths = new ArrayList<>();

    Event event = Event.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .category(category)
        .latitude(dto.getLatitude())
        .longitude(dto.getLongitude())
        .eventDate(dto.getEventDate().atStartOfDay())
        .uploadDate(LocalDateTime.now())
        .creatorId(userId) //* Id del usuario que crea el evento
        .build();

    // 2. Procesar multimedia (Si vienen)
    if (multimedia != null && !multimedia.isEmpty()) {
      try {
        for (MultipartFile file : multimedia) {
          // a. Subir al Object Storage
          String path = storageService.uploadFile(file, "events");

          // b. Agregar UUID a la lista de subidos
          uploadedFilesPaths.add(path);

          // c. Crear entidad Multimedia
          Multimedia media = Multimedia.builder()
              .objectStoragePath(path)
              .name(file.getOriginalFilename())
              .mimeType(file.getContentType())
              .build();

          // d. Asociar al evento
          event.addResource(media);
        }
      } catch (Exception e) {
        storageService.deleteFiles(uploadedFilesPaths);
        throw new RuntimeException("Unable to upload multimedia files", e);
      }
    }

    // 3. Guardar tod0 en Cascada
    try {
      Event savedEvent = eventRepository.save(event);
      submissionRepository.save(new SubmissionRequest(savedEvent));
      return EventOutputDTO.from(savedEvent);
    } catch(Exception e) {
      // En caso de error, eliminar los archivos subidos
      storageService.deleteFiles(uploadedFilesPaths);
      throw new RuntimeException("Unable to upload event and multimedia", e);
    }
  }

  private void validateFiles(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) return;

    for (MultipartFile file : files) {
      // Comparamos contra la lista inyectada
      if (!allowedMimeTypes.contains(file.getContentType())) {
        throw new IllegalArgumentException(
            "Non allowed type: " + file.getContentType() +
                ". Refer to the allowed types: " + allowedMimeTypes
        );
      }
    }
  }

  @Override
  public void update(String userId, Long eventId, EventDTO dto) {

    //TODO: Validar si el usuario puede realizar esta peticion

    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException("Event not found - Id: " + eventId));

    if (!event.getCreatorId().equals(userId))
      throw new UnauthorizedException("Unauthorized action: event ownership mismatch");

    if (event.getUploadDate().plusDays(7).isBefore(LocalDateTime.now()))
      throw new IllegalStateException("Event editing window has expired. Modifications are no longer allowed");

    if (!event.isAccepted())
      throw new IllegalStateException("Only accepted events can be edited");

    Category category = categoryService.findOrCreate(dto.getCategory());

    EventEditionRequest newEditionRequest = new EventEditionRequest(event, dto, category);

    editionRequestRepository.save(newEditionRequest);
  }

  @Override
  public EventOutputDTO deleteEvent(Long eventId) {
    Event event = eventRepository.findById(eventId)
        .orElseThrow(() -> new NotFoundException("Event not found - Id: " + eventId));

    event.markAsDeleted();
    eventRepository.save(event);

    return EventOutputDTO.from(event);
  }
}
