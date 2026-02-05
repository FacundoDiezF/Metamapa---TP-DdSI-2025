package ar.utn.edu.frba.ddsi.models.dtos.output;

import ar.utn.edu.frba.ddsi.models.entities.strategies.UrlGenerationStrategy;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import lombok.Data;

@Data
public class SubmissionRequestOutputDTO {
  private Long id;
  private SubmissionState submissionState;
  private EventOutputDTO event;

  public static SubmissionRequestOutputDTO from(SubmissionRequest submissionRequest) {
    SubmissionRequestOutputDTO dto = SubmissionRequestOutputDTO.initializeDTO(submissionRequest);
    dto.setEvent(EventOutputDTO.from(submissionRequest.getEvent()));
    return dto;
  }

  public static SubmissionRequestOutputDTO from(SubmissionRequest submissionRequest, UrlGenerationStrategy urlGenerationStrategy) {
    SubmissionRequestOutputDTO dto = SubmissionRequestOutputDTO.initializeDTO(submissionRequest);
    dto.setEvent(EventOutputDTO.from(submissionRequest.getEvent(), urlGenerationStrategy));
    return dto;
  }

  private static SubmissionRequestOutputDTO initializeDTO(SubmissionRequest submissionRequest) {
    SubmissionRequestOutputDTO dto = new SubmissionRequestOutputDTO();
    dto.setId(submissionRequest.getId());
    dto.setSubmissionState(submissionRequest.getState());
    return dto;
  }
}