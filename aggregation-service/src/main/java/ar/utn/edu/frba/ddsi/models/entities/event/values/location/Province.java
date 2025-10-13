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
@Table(name = "provinces")
public class Province {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter
  @Getter
  Long id;

  @Column(name = "name")
  @Getter
  String name;

  public Province(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Province province = (Province) o;
    return Objects.equals(this.name, province.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }
}
