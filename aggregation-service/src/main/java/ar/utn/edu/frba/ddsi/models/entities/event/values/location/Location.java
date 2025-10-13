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
}
