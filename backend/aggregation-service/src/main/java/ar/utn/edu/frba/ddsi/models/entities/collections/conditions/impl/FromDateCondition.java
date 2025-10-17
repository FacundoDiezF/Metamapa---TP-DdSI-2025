package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(force = true)
@Entity
@DiscriminatorValue("FROM_DATE")
public class FromDateCondition extends Condition {

  @Column(name = "from_date")
  private final LocalDateTime from;

  public FromDateCondition(LocalDateTime from) {
    this.from = from;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return event.getEventDate().isAfter(from);
  }
}