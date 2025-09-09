package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("CATEGORY")
public class CategoryCondition extends Condition {
  @ManyToOne //TODO Cuando se implemente la deduplicación de Conditions, esto debería ser OneToOne
  @JoinColumn(name = "category_id")
  private final Category category;

  public CategoryCondition(Category category) {
    this.category = category;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return Objects.equals(this.category.getId(), event.getCategory().getId());
  }
}
