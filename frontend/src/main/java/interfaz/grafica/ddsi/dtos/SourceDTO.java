package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class SourceDTO {
  Long id;
  Long clientId;
  Origin type;

  public SourceDTO(Long id, Long clientId, Origin type) {
    this.id = id;
    this.clientId = clientId;
    this.type = type;
  }
}
