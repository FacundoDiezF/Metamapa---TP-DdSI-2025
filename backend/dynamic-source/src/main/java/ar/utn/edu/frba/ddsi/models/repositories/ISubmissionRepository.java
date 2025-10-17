package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionRequest;
import ar.utn.edu.frba.ddsi.models.entities.submission.SubmissionState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubmissionRepository extends JpaRepository<SubmissionRequest, Long> {

List<SubmissionRequest> findByState(SubmissionState state);
}
