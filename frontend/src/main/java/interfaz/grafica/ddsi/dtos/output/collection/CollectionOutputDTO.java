package interfaz.grafica.ddsi.dtos.output.collection;

import java.util.List;
import lombok.Data;

@Data
public class CollectionOutputDTO {
  String title;
  String description;
  List<ConditionDTO> conditions;
  List<Long> sourceIds;
  ConsensusType consensusType;
}
