package ar.utn.edu.frba.ddsi.models.entities.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "provinces")
public class Province {

  @Id
  Long id;

  @Column(name = "name")
  String name;

  public Province(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
