package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Province;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, Long> {

  Province findByName(String name);

  //TODO: Ver si esto anda bien
  @Query("""
      SELECT new ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO(p.name, COUNT(e.id))
      FROM Province p
      JOIN Event e ON e.province = p
      JOIN Category c ON e.category.id = c.id
      WHERE c.id = :categoryId
      GROUP BY p.id
      ORDER BY COUNT(e.id) DESC
      LIMIT :count
      """)
  List<TopOutputDTO> findTopByCategory(
      @Param("categoryId") Long categoryId,
      @Param("count") Integer count
  );

  //TODO: Ver si esto anda bien
  @Query("""
      SELECT new ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO(p.name, COUNT(e.id))
      FROM Province p
      JOIN Event e ON e.province = p
      JOIN Collection c ON e MEMBER OF c.events
      WHERE c.handler = :collectionHandler
      GROUP BY p.id
      ORDER BY COUNT(e.id) DESC
      LIMIT :count
      """)
  List<TopOutputDTO> findTopByCollection(
      @Param("collectionHandler") String collectionHandler,
      @Param("count") Integer count
  );
}
