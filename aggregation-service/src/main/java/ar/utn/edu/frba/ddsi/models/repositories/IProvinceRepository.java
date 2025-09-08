package ar.utn.edu.frba.ddsi.models.repositories;


import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProvinceRepository extends JpaRepository<Province,String> {
  //List<Province> findProvinceBy(String by, String id, String order, Integer count);
}
