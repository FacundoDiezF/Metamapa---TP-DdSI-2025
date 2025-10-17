package ar.utn.edu.frba.ddsi.models.dtos.input;

import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import lombok.Data;

@Data
public class SubmissionEvaluationDTO {
  String reviewer; //TODO: Esto deberia ser un usuario
  SubmissionState state;
  String suggestion;
}
