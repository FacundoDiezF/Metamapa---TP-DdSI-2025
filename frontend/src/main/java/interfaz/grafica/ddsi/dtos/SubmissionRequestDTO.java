package interfaz.grafica.ddsi.dtos;

import lombok.Data;

@Data
public class SubmissionRequestDTO {
  private Long id;
  private SubmissionState submissionState;
  private EventDTO event;
}