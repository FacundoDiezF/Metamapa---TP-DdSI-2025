package ar.utn.edu.frba.ddsi.models.entities.event;

import lombok.Getter;

@Getter
public class Category {
  private final String name;

  public Category(String name) {
    this.name = name;
  }
}
