package ar.utn.edu.frba.ddsi.models.entities.event.values;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "categories")

public class Category{
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private final String name;
    public Category(String name) {
    this.name = name;
  }
}
