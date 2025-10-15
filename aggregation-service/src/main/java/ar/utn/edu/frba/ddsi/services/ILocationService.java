package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.event.ProvinceDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ILocationService {
  Location findOrCreate(Double latitude, Double longitude);

  List<Location> findOrCreateLocationByCoordinates(Set<Coordinate> coordinates);

  Page<ProvinceDTO> getAllProvinces(Pageable pageable);
}