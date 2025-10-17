package ar.utn.edu.frba.ddsi.models.dtos.output.api;

import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Coordinate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public  class LocationOutputDTO {
  @JsonProperty("lat")
  private Double lat;
  @JsonProperty("lon")
  private Double lon;
  @JsonProperty("aplanar")
  @Builder.Default
  private Boolean flatten = true;

  public static LocationOutputDTO from(Coordinate coordinate) {
    return LocationOutputDTO.builder()
        .lat(coordinate.getLatitude())
        .lon(coordinate.getLongitude())
        .build();
  }

  public static List<LocationOutputDTO> from(Set<Coordinate> coordinates) {
    return coordinates.stream().map(LocationOutputDTO::from).toList();
  }
}