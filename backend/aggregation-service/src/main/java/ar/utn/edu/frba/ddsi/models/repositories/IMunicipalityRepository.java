package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMunicipalityRepository extends JpaRepository<Municipality, Long> {
  List<Municipality> findByNameIn(List<String> names);
}
