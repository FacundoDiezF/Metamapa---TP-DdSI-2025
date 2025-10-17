package ar.utn.edu.frba.ddsi.models.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SourceInputDTO {
  @JsonProperty("file_name")
  String fileName;
  @JsonProperty("import_strategy")
  String importStrategy;
}