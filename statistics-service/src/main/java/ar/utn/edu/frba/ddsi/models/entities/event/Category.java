package ar.utn.edu.frba.ddsi.models.entities.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

  @Id
  private Long id;

  @Column(name = "name")
  private String name;
}
