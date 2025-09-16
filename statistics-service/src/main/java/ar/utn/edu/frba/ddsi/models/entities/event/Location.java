package ar.utn.edu.frba.ddsi.models.entities.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Location {

  @ManyToOne
  @JoinColumn(name = "province_id")
  Province province;

}
