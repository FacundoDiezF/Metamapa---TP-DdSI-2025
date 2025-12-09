package ar.utn.edu.frba.ddsi.models.dtos.output.collection;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import java.util.ArrayList;
import lombok.Data;

import java.util.List;

@Data
public class CollectionOutputDTO {

  String handler;
  String title;
  String description;
  List<ConditionDTO> conditions;
  List<SourceOutputDTO> sources;
  ConsensusType consensusType;

  List<Long> eventIds;


  public static CollectionOutputDTO from(Collection collection) {
    CollectionOutputDTO dto = new CollectionOutputDTO();
    dto.handler = collection.getHandler();
    dto.title = collection.getTitle();
    dto.description = collection.getDescription();
    dto.conditions = collection.getCollectionCriteria().getConditions().stream().map(ConditionDTO::from).toList();
    dto.sources = collection.getSources().stream().map(SourceOutputDTO::from).toList();
    dto.consensusType = collection.getConsensusType();
    dto.eventIds = collection.getEventsIds();
    return dto;
  }
}
