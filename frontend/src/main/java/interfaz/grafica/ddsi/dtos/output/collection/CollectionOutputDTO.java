package interfaz.grafica.ddsi.dtos.output.collection;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class CollectionOutputDTO {
  String title;
  String description;
  List<ConditionDTO> conditions = new ArrayList<>();
  List<Long> sourceIds;
  ConsensusType consensusType;
}
