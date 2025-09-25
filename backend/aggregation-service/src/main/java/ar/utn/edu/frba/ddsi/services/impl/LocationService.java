package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import org.springframework.stereotype.Service;

@Service
public class LocationService implements ILocationService {

  @Override
  public Location findOrCreate(Double latitude, Double longitude) {
    // TODO se fija si ya existe la location en la BBDD. La API est'a en los modulos.
    return Location.builder()
        .latitude(latitude)
        .longitude(longitude)
        .build();
  }
}
