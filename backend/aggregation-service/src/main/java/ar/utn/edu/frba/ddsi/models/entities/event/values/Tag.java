package ar.utn.edu.frba.ddsi.models.entities.event.values;

import lombok.Getter;

@Getter
public class Tag {
  private final String name;

  public Tag(String name) {
    this.name = name;
  }

}
