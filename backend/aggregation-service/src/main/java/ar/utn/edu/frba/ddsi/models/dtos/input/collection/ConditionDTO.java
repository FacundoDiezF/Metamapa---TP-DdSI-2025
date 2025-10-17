package ar.utn.edu.frba.ddsi.models.dtos.input.collection;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ConditionType;
import lombok.Data;

@Data
public class ConditionDTO {
  ConditionType conditionType;
  String conditionValue; // Depende del ConditionType se castea a un tipo de dato (Category,String,LocalDate)
}
