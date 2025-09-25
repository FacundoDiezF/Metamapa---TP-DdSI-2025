package ar.utn.edu.frba.ddsi.models.dtos.input;

import lombok.Data;

@Data
public class SubscriberInputDTO {

  private String callbackUrl;
  private Long sourceClientId;
}
