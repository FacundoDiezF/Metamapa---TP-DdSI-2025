package ar.utn.edu.frba.ddsi.models.dtos.input.collection;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import java.util.List;
import lombok.Data;

@Data
public class CollectionCreationDTO {
  String title;
  String description;
  List<ConditionDTO> conditions;
  List<Long> sourceIds;
  ConsensusType consensusType;
}
