package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ICondition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import java.time.LocalDateTime;

public class FromDateCondition implements ICondition {

  private final LocalDateTime from;

  public FromDateCondition(LocalDateTime from) {
    this.from = from;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return event.getEventDate().isAfter(from);
  }
}