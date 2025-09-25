package ar.utn.edu.frba.ddsi.models.dtos.input.deletion_request;

import lombok.Data;

@Data
public class DeletionRequestEvaluationDTO {
  Long deletionRequestId;
  String evaluatorName; //TODO: Esto deberia ser un usuario
  boolean accepted;
}
