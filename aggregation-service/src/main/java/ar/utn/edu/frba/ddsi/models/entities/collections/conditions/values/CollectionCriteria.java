package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.util.List;

public class CollectionCriteria implements ICondition{

  final List<ICondition> conditions;

  public static CollectionCriteria from(List<ConditionDTO> conditionsDto) {
    List<ICondition> conditions = conditionsDto.stream().map(c -> c.getConditionType().toCondition(c.getConditionValue())).toList();
    return new CollectionCriteria(conditions);
  }

  public CollectionCriteria(List<ICondition> conditions) {
    this.conditions = conditions;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return conditions.stream().allMatch(condition -> condition.isSatisfiedBy(event));
  }
}
