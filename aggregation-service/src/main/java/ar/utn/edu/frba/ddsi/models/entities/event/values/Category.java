package ar.utn.edu.frba.ddsi.models.entities.event.values;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class Category {
  private final String name;
}
