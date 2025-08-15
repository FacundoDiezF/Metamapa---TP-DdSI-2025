package ar.utn.edu.frba.ddsi.models.entities.spamDetector;

import ar.utn.edu.frba.ddsi.models.entities.deletion_request.DeletionRequest;
import java.util.List;

public interface ISpamDetector {
  boolean isSpam(List<DeletionRequest> eventDeletionRequests, String argument);
}
