package ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request;

import lombok.Data;

@Data
public class DeletionRequestEvaluationDTO {
  Long deletionRequestId;
  boolean accepted;
}
