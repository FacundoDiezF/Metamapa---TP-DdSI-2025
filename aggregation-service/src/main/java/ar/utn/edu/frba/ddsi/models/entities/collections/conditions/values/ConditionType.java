package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values;

import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.CategoryCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.FromDateCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.TitleCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.UntilDateCondition;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import java.time.LocalDateTime;

public enum ConditionType {

  CATEGORY {
    @Override
    public ICondition toCondition(String conditionValue) {

      Category category = new Category(conditionValue);
      return new CategoryCondition(category);
    }
  },

  TITLE {
    @Override
    public ICondition toCondition(String conditionValue) {

      return new TitleCondition(conditionValue);
    }
  },

  FROM_DATE {
    @Override
    public ICondition toCondition(String conditionValue) {

      return new FromDateCondition(LocalDateTime.parse(conditionValue));
    }
  },

  TO_DATE {
    @Override
    public ICondition toCondition(String conditionValue) {

      return new UntilDateCondition(LocalDateTime.parse(conditionValue));
    }
  };

  public abstract ICondition toCondition(String conditionValue);
}
