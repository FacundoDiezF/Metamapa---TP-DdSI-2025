package ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(force = true)
@Entity
@Table(name = "collection_criterias")
public class CollectionCriteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToMany
  @JoinTable(
      name = "criterias_conditions",
      joinColumns = @JoinColumn(name = "criteria_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "condition_id", referencedColumnName = "id")
  )
  private final List<Condition> conditions;
  //TODO: Al crear un CollectionCriteria, debería fijarme si las condition ya existen para no duplicarlas
  //TODO: Al borrar el CollectionCriteria, si solo ese usa alguna Condition, tambien deberia borrarla

  public CollectionCriteria(List<Condition> conditions) {
    this.conditions = conditions;
  }

  public boolean isSatisfiedBy(Event event) {
    return conditions.stream().allMatch(condition -> condition.isSatisfiedBy(event));
  }
}
