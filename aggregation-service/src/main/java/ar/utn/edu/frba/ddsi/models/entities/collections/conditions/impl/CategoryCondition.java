package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ICondition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;

public class CategoryCondition implements ICondition {
  private final Category category;

  public CategoryCondition(Category category) {
    this.category = category;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    String conditionCategory = category.getName();
    String eventCategory = event.getCategory().getName();

    return conditionCategory.equals(eventCategory);
  }

  // TODO: Se compara la cotegoria a partir del String, es correcto?

}
