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
@DiscriminatorValue("UNTIL_DATE")
public class UntilDateCondition extends Condition {

  @Column(name = "until_date")
  final LocalDateTime to;

  public UntilDateCondition(LocalDateTime to) {
    this.to = to;
  }

  @Override
  public boolean isSatisfiedBy(Event event) {
    return event.getEventDate().isBefore(to);
  }
}
