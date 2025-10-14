package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionCreationDTO;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.CollectionUpdateDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.collection.CollectionOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.event.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.services.ICollectionService;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collections")
public class CollectionController {

  private final ICollectionService collectionService;

  public CollectionController(ICollectionService collectionService) {
    this.collectionService = collectionService;
  }

  @GetMapping
  public Page<CollectionOutputDTO> getCollections(
      @RequestParam (required = false) LocalDateTime fromUpdatedDate,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());

    return collectionService.getCollections(fromUpdatedDate, pageable);
  }

  @PostMapping
  public CollectionOutputDTO createCollection(@RequestBody CollectionCreationDTO collectionCreationDTO) {
    return collectionService.create(collectionCreationDTO);
  }

  @GetMapping("/{handler}/events")
  public List<EventOutputDTO> getEventsFromCollection(
      @PathVariable String handler,
      @RequestParam(required = false) boolean curedNavigation,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) LocalDateTime untilUploadDate,
      @RequestParam(required = false) LocalDateTime fromUploadDate,
      @RequestParam(required = false) LocalDateTime untilEventDate,
      @RequestParam(required = false) LocalDateTime fromEventDate
  ) {
    return collectionService.getEventsFromCollection(curedNavigation, handler, category,
        untilUploadDate, fromUploadDate, untilEventDate, fromEventDate);
  }

  @PutMapping("/{handler}")
  public void updateCollection(@PathVariable String handler, @RequestBody CollectionUpdateDTO dto) {
    collectionService.updateCollection(handler, dto);
  }

  @DeleteMapping("/{handler}")
  public void deleteCollection(@PathVariable String handler) {
    collectionService.deleteCollection(handler);
  }

  @GetMapping("/{handler}")
  public CollectionOutputDTO getCollectionByHandler(@PathVariable String handler) {
    return collectionService.getCollectionByHandler(handler);
  }

  @PatchMapping("/{handler}/consensus")
  public void updateConsensus(@PathVariable String handler, @RequestBody ConsensusType consensusType) {
    collectionService.updateConsensus(handler, consensusType);
  }

  @PostMapping("/{handler}/sources/{sourceId}")
  public void addSource(@PathVariable String handler, @PathVariable Long sourceId) {
    collectionService.addSource(handler, sourceId);
  }

  @DeleteMapping("/{handler}/sources/{sourceId}")
  public void removeSource(@PathVariable String handler, @PathVariable Long sourceId) {
    collectionService.removeSource(handler, sourceId);
  }
}
