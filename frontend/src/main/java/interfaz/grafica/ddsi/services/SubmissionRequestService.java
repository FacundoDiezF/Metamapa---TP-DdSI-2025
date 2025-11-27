package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.SubmissionRequestDTO;
import interfaz.grafica.ddsi.dtos.output.SubmissionRequestEvaluationDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SubmissionRequestService {

  private final ServerAdapter server;

  public SubmissionRequestService(ServerAdapter server) {
    this.server = server;
  }

  public PageDTO<SubmissionRequestDTO> getAll(int page, int size) {
    return server.getAllSubmissionRequest(page, size);
  }

  public void evaluate(Long submissionId, SubmissionRequestEvaluationDTO dto) {
    try {
      server.evaluateSubmissionRequest(submissionId, dto);
    } catch (WebClientResponseException e) {
      throw new BackendException(e);
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }
}
