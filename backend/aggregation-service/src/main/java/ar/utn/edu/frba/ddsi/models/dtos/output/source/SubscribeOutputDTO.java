package ar.utn.edu.frba.ddsi.models.dtos.output.source;

import lombok.Data;

@Data
public class SubscribeOutputDTO {
  Long sourceClientId;
  String callbackUrl;

  public SubscribeOutputDTO(Long sourceClientId, String callbackUrl) {
    this.sourceClientId = sourceClientId;
    this.callbackUrl = callbackUrl;
  }
}
