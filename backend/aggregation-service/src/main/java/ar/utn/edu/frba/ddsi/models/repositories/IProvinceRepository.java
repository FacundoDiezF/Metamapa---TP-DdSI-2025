package ar.utn.edu.frba.ddsi.models.repositories;


import ar.utn.edu.frba.ddsi.models.entities.event.values.location.Province;
import java.util.List;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Long> {

  List<Province> findByNameIn(List<String> names);

  Page<Province> findAll(@NonNull Pageable pageable);
}
