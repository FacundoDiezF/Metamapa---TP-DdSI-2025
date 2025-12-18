package interfaz.grafica.ddsi.dtos;

import interfaz.grafica.ddsi.dtos.output.collection.ConditionDTO;
import interfaz.grafica.ddsi.dtos.output.collection.ConsensusType;
import java.util.List;
import lombok.Data;

@Data
public class CollectionDTO {
  String handler;
  String title;
  String description;
  List<ConditionDTO> conditions;
  List<SourceDTO> sources;
  ConsensusType consensusType;

  List<Long> eventIds;
}
