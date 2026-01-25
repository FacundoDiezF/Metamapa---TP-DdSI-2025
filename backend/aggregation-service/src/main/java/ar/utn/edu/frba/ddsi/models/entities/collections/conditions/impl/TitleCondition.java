package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.ConditionType;
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
  final String searchTitle;

  public TitleCondition(String searchTitle) {
    this.searchTitle = searchTitle.toLowerCase();
  }

  @Override
  public boolean isSatisfiedBy(Event event) {

    String eventTitleLower = event.getTitle().toLowerCase();

    return eventTitleLower.contains(searchTitle);
  }

  @Override
  public String getConditionValue() {
    return searchTitle;
  }

  @Override
  public ConditionType getType() {
    return ConditionType.TITLE;
  }

}
