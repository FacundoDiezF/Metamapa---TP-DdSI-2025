package ar.utn.edu.frba.ddsi.models.repositories;


import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Province;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, String> {

}
