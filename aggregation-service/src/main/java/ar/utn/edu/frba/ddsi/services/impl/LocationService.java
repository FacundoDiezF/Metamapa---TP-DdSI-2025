package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.adapters.LocationAPI;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Location;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LocationService implements ILocationService {

  private final LocationAPI locationAPI;

  public LocationService(LocationAPI locationAPI) {
    this.locationAPI = locationAPI;
  }

  @Override
  public Location findOrCreate(Double latitude, Double longitude) {
    // TODO se fija si ya existe la location en la BBDD. La API esta en los modulos.
    return Location.builder()
        .latitude(latitude)
        .longitude(longitude)
        .build();
  }

  @Override
  public Map<Location, List<Event>> findOrCreateLocationByEvents(List<Event> events){

    Map<Location, List<Event>> locationEventMap = locationAPI.getLocationsByEvents(events);



//    Map<Province, List<Event>> provinceEventMap = locationAdapter.getProvincesByEvents(events);
//
//    Set<String> provinceNames = provinceEventMap.keySet().stream()
//        .map(Province::getName)
//        .collect(Collectors.toSet());
//
//    List<Province> persistedProvinces = provinceRepository.findAllByName(provinceNames);
//    Map<String, Province> persistedMap = persistedProvinces.stream()
//        .collect(Collectors.toMap(Province::getName, Function.identity()));
//
//    for (String name : provinceNames) {
//      if (!persistedMap.containsKey(name)) {
//        Province newProvince = new Province(name);
//        Province saved = provinceRepository.save(newProvince);
//        persistedMap.put(name, saved);
//      }
//    }

    return null;
  }
}
