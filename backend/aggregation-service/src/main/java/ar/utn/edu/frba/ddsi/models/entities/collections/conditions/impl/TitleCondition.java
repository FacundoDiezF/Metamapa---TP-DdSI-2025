package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("TITLE")
public class TitleCondition extends Condition {
  @Column(name = "title")
  final String title;

  public TitleCondition(String title) {
    this.title = title;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return this.title.equals(event.getTitle());
  }

}
