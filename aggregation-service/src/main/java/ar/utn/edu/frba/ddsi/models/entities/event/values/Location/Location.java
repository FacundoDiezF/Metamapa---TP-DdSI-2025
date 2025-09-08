package ar.utn.edu.frba.ddsi.models.entities.event.values.Location;

import jakarta.persistence.ManyToOne;

public class Location {

  Long latitude;

  Long longitude;

  @ManyToOne
  Province province;
}
