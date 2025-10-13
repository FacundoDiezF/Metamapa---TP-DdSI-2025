package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.adapters.LocationAPI;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Department;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Municipality;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Province;
import ar.utn.edu.frba.ddsi.models.repositories.IDepartmentRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IMunicipalityRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LocationService implements ILocationService {

  private final LocationAPI locationAPI;
  private final IProvinceRepository provinceRepository;
  private final IMunicipalityRepository municipalityRepository;
  private final IDepartmentRepository departmentRepository;

  public LocationService(LocationAPI locationAPI,
                         IProvinceRepository provinceRepository,
                         IMunicipalityRepository municipalityRepository,
                         IDepartmentRepository departmentRepository) {
    this.locationAPI = locationAPI;
    this.provinceRepository = provinceRepository;
    this.municipalityRepository = municipalityRepository;
    this.departmentRepository = departmentRepository;
  }

  @Override
  public Location findOrCreate(Double latitude, Double longitude) { //TODO Test
    // TODO se fija si ya existe la location en la BBDD. La API esta en los modulos.
    Coordinate coordinate = Coordinate.builder()
        .latitude(latitude)
        .longitude(longitude)
        .build();

    return Location.builder()
        .coordinate(coordinate)
        .build();
  }

  @Override
  public List<Location> findOrCreateLocationByCoordinates(Set<Coordinate> coordinates){ //TODO Test

    //1. Get locations from API
    List<Location> locations = locationAPI.getLocationsByCoordinate(coordinates);

    //2. Separate provinces, municipalities and departments in individual lists
    Set<Province> provinces = new HashSet<>();
    Set<Municipality> municipalities = new HashSet<>();
    Set<Department> departments = new HashSet<>();

    for (Location location : locations) { //In this case it suffices with a set because for the three classes, equals() and hashCode() were overwritten
      if (location.getProvince() != null && location.getProvince().getName() != null) {
        provinces.add(location.getProvince());
        //TODO Try this so every location references a province object that is in the provinces set
        //*This is to avoid having multiple Province objects with the same name but different references
        //*Which leads to the list of locations having objects without ids
        if(!provinces.add(location.getProvince())){
          location.setProvince(provinces
              .stream()
              .filter(p -> p.equals(location.getProvince()))
              .findFirst()
                  .orElseThrow(
                      () -> new RuntimeException("Province" + location.getProvince().getName() + "found within the set, but equals couldn't be obtained")
                  ));
        }
      }
      if (location.getMunicipality() != null && location.getMunicipality().getName() != null) {
        municipalities.add(location.getMunicipality());
        //TODO Try this so every location references a municipality object that is in the municipalities set
        //*This is to avoid having multiple Municipality objects with the same name but different references
        //*Which leads to the list of locations having objects without ids
        if(!municipalities.add(location.getMunicipality())){
          location.setMunicipality(municipalities
              .stream()
              .filter(m -> m.equals(location.getMunicipality()))
              .findFirst()
              .orElseThrow(
                  () -> new RuntimeException("Municipality" + location.getMunicipality().getName() + "found within the set, but equals couldn't be obtained")
              ));
        }
      }
      if (location.getDepartment() != null && location.getDepartment().getName() != null) {
        departments.add(location.getDepartment());
        //TODO Try this so every location references a department object that is in the departments set
        //*This is to avoid having multiple Department objects with the same name but different references
        //*Which leads to the list of locations having objects without ids
        if(!departments.add(location.getDepartment())){
          location.setDepartment(departments
              .stream()
              .filter(d -> d.equals(location.getDepartment()))
              .findFirst()
              .orElseThrow(
                  () -> new RuntimeException("Department" + location.getDepartment().getName() + "found within the set, but equals couldn't be obtained")
              ));
        }
      }
    }

    //3. Persist provinces, municipalities and departments
    //TODO Creo que esto no funciona bien porque no esta verificando si ya existen en base al nombre, manejar eso
    provinceRepository.saveAll(provinces);
    municipalityRepository.saveAll(municipalities);
    departmentRepository.saveAll(departments);

    //4. Return locations
    return locations;
  }
}
