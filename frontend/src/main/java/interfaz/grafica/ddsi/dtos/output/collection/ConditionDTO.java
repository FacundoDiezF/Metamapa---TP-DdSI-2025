package interfaz.grafica.ddsi.dtos.output.collection;

import lombok.Data;

@Data
public class ConditionDTO {
  ConditionType conditionType;
  String conditionValue; // Depende del ConditionType se castea a un tipo de dato (Category,String,LocalDate)
}
