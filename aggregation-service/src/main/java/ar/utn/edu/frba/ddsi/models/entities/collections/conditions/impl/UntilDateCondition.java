package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ICondition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;

public class UntilDateCondition implements ICondition {

  final LocalDateTime to;

  public UntilDateCondition(LocalDateTime to) {
    this.to = to;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return event.getEventDate().isBefore(to);
  }
}
