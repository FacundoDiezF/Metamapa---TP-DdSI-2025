package ar.utn.edu.frba.ddsi.models.dtos.input;

import java.util.List;
import lombok.Data;

@Data
public class CollectionIntputDTO {
  String handler;
  List<Long> eventIds;
}
