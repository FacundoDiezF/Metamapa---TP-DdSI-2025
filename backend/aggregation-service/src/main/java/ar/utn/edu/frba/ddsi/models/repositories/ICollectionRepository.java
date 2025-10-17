package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.collections.Collection;
import java.time.LocalDateTime;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICollectionRepository extends JpaRepository<Collection, Long> {
  Collection findByHandler(String handler);

  Page<Collection> findAllByUpdatedDateAfter(LocalDateTime updatedDate, Pageable pageable);
  Page<Collection> findAll(@NonNull Pageable pageable);
}