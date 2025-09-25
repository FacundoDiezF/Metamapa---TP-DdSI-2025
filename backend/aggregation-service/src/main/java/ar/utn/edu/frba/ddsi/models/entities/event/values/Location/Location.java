package ar.utn.edu.frba.ddsi.models.entities.event.values.Location;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Location {

  @Column(name= "latitude")
  Double latitude;

  @Column(name= "longitude")
  Double longitude;

  @ManyToOne
  @JoinColumn(name = "province_id")//TODO nullable = false
  Province province;
}
