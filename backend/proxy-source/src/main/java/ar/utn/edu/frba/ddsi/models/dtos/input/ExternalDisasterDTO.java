package ar.utn.edu.frba.ddsi.models.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class ExternalDisasterDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  @JsonProperty("fecha_hecho")
  private ZonedDateTime eventDate;
  @JsonProperty("created_at")
  private ZonedDateTime createdAt;
  @JsonProperty("updated_at")
  private ZonedDateTime updatedAt;

}