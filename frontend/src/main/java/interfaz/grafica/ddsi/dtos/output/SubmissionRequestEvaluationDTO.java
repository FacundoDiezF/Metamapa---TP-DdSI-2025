package interfaz.grafica.ddsi.dtos.output;

import interfaz.grafica.ddsi.dtos.SubmissionState;
import lombok.Data;

@Data
public class SubmissionRequestEvaluationDTO {
  SubmissionState state;
  String suggestion;

  public SubmissionRequestEvaluationDTO(boolean accepted, String suggestion) {
    if (accepted) {
      if (suggestion == null) {
        this.state = SubmissionState.ACCEPTED;
      } else {
        this.state = SubmissionState.ACCEPTED_WITH_SUGGESTIONS;
      }
    } else {
      this.state = SubmissionState.REJECTED;
    }
    this.suggestion = suggestion;
  }
}
