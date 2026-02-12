package ar.utn.edu.frba.ddsi.models.dtos.input.collection;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ConditionType;
import lombok.Data;

@Data
public class ConditionDTO {
  ConditionType conditionType;
  String conditionValue; // Depende del ConditionType se castea a un tipo de dato (Category,String,LocalDate)

  public static ConditionDTO from(Condition condition) {
    ConditionDTO dto = new ConditionDTO();
    dto.conditionType = condition.getType();
    dto.conditionValue = condition.getConditionValue();
    return dto;
  }

  // Setter that ensures conditionType is stored in lowercase (!! Podria afectar si en el futuro se agregan ConditionTypes con mayusculas internas).
  public void setConditionValue(String conditionValue) {
    this.conditionValue = (conditionValue != null) ? conditionValue.toLowerCase() : null;
  }
}
