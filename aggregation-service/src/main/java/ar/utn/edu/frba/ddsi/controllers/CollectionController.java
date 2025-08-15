package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.services.impl.CollectionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collections")
public class CollectionController {

  private final CollectionService collectionService;

  public CollectionController(CollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @GetMapping
  public List<CollectionOutputDTO> getCollections() {
    return collectionService.getCollections();
  }

  @PostMapping
  public void createCollection(@RequestBody CollectionCreationDTO collectionCreationDTO) {
    collectionService.create(collectionCreationDTO);
  }

  @GetMapping("/{handler}/events")
  public List<EventOutputDTO> getEventsFromCollection(
      @PathVariable String handler,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) LocalDateTime untilUploadDate,
      @RequestParam(required = false) LocalDateTime fromUploadDate,
      @RequestParam(required = false) LocalDateTime untilEventDate,
      @RequestParam(required = false) LocalDateTime fromEventDate
  ) {
    return collectionService.getEventsFromCollection(handler, category,
        untilUploadDate, fromUploadDate, untilEventDate, fromEventDate);
  }

  //! Para testear con el postman
  @PostMapping("/refresh")
  public void refreshCollections(LocalDateTime lastUpdate) {
    collectionService.refreshCollections(lastUpdate);
  }
}
