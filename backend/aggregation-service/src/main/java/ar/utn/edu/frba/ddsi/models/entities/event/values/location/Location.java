package ar.utn.edu.frba.ddsi.models.entities.event.values.location;

import ar.utn.edu.frba.ddsi.models.dtos.input.api.APILocationInputDTO;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Embeddable
public class Location {

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "latitude", column = @Column(name = "latitude")),
      @AttributeOverride(name = "longitude", column = @Column(name = "longitude"))
  })
  private Coordinate coordinate;

  @ManyToOne
  @JoinColumn(name = "province_id")
  Province province;

  @ManyToOne
  @JoinColumn(name = "municipality_id")
  Municipality municipality;

  @ManyToOne
  @JoinColumn(name = "department_id")
  Department department;

  public static Location from(APILocationInputDTO dto) {


    return Location.builder()
        .coordinate(Coordinate.builder()
            .latitude(dto.getLat())
            .longitude(dto.getLon())
            .build())
        .province(dto.getProvince() != null ? new Province(dto.getProvince()) : null)
        .municipality(dto.getMunicipality() != null ? new Municipality(dto.getMunicipality()) : null)
        .department(dto.getDepartment() != null ? new Department(dto.getDepartment()) : null)
        .build();
  }

  public static List<Location> from(List<APILocationInputDTO> dtos) {
    return dtos.stream().map(Location::from).toList();
  }
  //
  //*This is to avoid having multiple Province/Municipality/Department objects with the same name in the location list.
  //*If not done, only the objects first found (which are contained within the set) would have an assigned id,
  //* and the rest, even though they are equal, would have a null one. That's why we reassign it.
  public <T> void deduplicateAndSet(
      T entity,
      Set<T> set,
      BiConsumer<Location, T> setter,
      Function<T, String> nameGetter,
      String entityType
  ) {
    if (entity != null && nameGetter.apply(entity) != null) {
      if (!set.add(entity)) {
        setter.accept(this, set.stream()
            .filter(e -> e.equals(entity))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                entityType + " \"" + nameGetter.apply(entity) + "\" found within the set, but equals couldn't be obtained"
            )));
      }
    }
  }
}
