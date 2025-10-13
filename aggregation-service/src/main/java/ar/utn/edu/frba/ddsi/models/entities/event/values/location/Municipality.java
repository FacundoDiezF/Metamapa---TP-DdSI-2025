package ar.utn.edu.frba.ddsi.models.entities.event.values.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "municipalities")
public class Municipality {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter
  @Getter
  Long id;

  @Column(name = "name")
  @Getter
  String name;

  public Municipality(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) { //TODO test
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Municipality municipality = (Municipality) o;
    return Objects.equals(this.name, municipality.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }
}
