package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.output.event.ProvinceDTO;
import ar.utn.edu.frba.ddsi.models.entities.adapters.LocationAPI;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Department;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Location;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Municipality;
import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Province;
import ar.utn.edu.frba.ddsi.models.repositories.IDepartmentRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IMunicipalityRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IProvinceRepository;
import ar.utn.edu.frba.ddsi.services.ILocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    //2. Persist locations
    this.persistLocations(locations);

    //3. Return locations
    return locations;
  }

  private void persistLocations(List<Location> locations){
    Set<Province> provinces = new HashSet<>();
    Set<Municipality> municipalities = new HashSet<>();
    Set<Department> departments = new HashSet<>();

    //1. Separate provinces, municipalities and departments in individual sets
    this.deduplicateLocations(locations, provinces, municipalities, departments);

    //2. Persist non-persisted provinces, municipalities and departments, and retrieve persisted ones (avoiding duplication)
    this.persistNonPersisted(provinces, municipalities, departments);
  }

  private void deduplicateLocations(
      List<Location> locations,
      Set<Province> provinces,
      Set<Municipality> municipalities,
      Set<Department> departments
  ) {
    for (Location location : locations) {//In this case it suffices with a set because for the three classes, equals() and hashCode() were overwritten
      location.deduplicateAndSet(
          location.getProvince(),
          provinces,
          Location::setProvince,
          Province::getName,
          "Province"
      );
      location.deduplicateAndSet(
          location.getMunicipality(),
          municipalities,
          Location::setMunicipality,
          Municipality::getName,
          "Municipality"
      );
      location.deduplicateAndSet(
          location.getDepartment(),
          departments,
          Location::setDepartment,
          Department::getName,
          "Department"
      );
    }
  }

  private void persistNonPersisted(Set<Province> provinces, Set<Municipality> municipalities, Set<Department> departments) {
    this.persistNonPersisted(
        provinces,
        provinceRepository::findByNameIn,
        Province::getName,
        Province::getId,
        Province::setId,
        provinceRepository::saveAll
    );

    this.persistNonPersisted(
        municipalities,
        municipalityRepository::findByNameIn,
        Municipality::getName,
        Municipality::getId,
        Municipality::setId,
        municipalityRepository::saveAll
    );

    this.persistNonPersisted(
        departments,
        departmentRepository::findByNameIn,
        Department::getName,
        Department::getId,
        Department::setId,
        departmentRepository::saveAll
    );
  }

  private <T, ID> void persistNonPersisted(
      Set<T> set,
      Function<List<String>, List<T>> findByNameIn,
      Function<T, String> getName,
      Function<T, ID> getId,
      BiConsumer<T, ID> setId,
      Consumer<Iterable<T>> saveAll
  ) {
    // 1. Find existing by name
    Set<T> existing = new HashSet<>(findByNameIn.apply(
        set.stream().map(getName).toList()
    ));

    // 2. Set IDs for existing
    for (T entity : set) {
      existing.stream()
          .filter(e -> getName.apply(e).equals(getName.apply(entity)))
          .findFirst()
          .ifPresent(e -> setId.accept(entity, getId.apply(e)));
    }

    // 3. Save only new
    Set<T> toSave = set.stream()
        .filter(e -> getId.apply(e) == null)
        .collect(Collectors.toSet());

    // 4. Persist only new
    saveAll.accept(toSave);
  }

  @Override
  public Page<ProvinceDTO> getAllProvinces(Pageable pageable) {
    Page<Province> page = provinceRepository.findAll(pageable);
    return page.map(ProvinceDTO::from);
  }
}
