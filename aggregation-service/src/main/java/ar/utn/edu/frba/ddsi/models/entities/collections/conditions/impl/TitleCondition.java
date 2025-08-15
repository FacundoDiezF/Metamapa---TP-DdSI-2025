package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ICondition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;

public class TitleCondition implements ICondition {

  final String title;

  public TitleCondition(String title) {
    this.title = title;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return this.title.equals(event.getTitle());
  }

}
