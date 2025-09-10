package ar.utn.edu.frba.ddsi.models.repositories;


import ar.utn.edu.frba.ddsi.models.entities.event.values.Location.Province;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProvinceRepository extends JpaRepository<Province, String> {

  @Query("""
      SELECT p
      FROM Province p
      JOIN Event e ON e.location.province = p
      JOIN Category c ON e.category.id = c.id
      WHERE c.id = :categoryId
      GROUP BY p.id
      ORDER BY COUNT(e.id) DESC
      LIMIT :count
      """)
  List<Province> findTopByCategory(
      @Param("categoryId") Long categoryId,
      @Param("count") Integer count
  );

  @Query("""
      SELECT p
      FROM Province p
      JOIN Event e ON e.location.province = p
      JOIN Collection c ON e MEMBER OF c.events
      WHERE c.handler = :collectionId
      GROUP BY p.id
      ORDER BY COUNT(e.id) DESC
      LIMIT :count
      """)
  List<Province> findTopByCollection(
      @Param("collectionId") String collectionId,
      @Param("count") Integer count
  );

  List<Province> findAllByName(List<String> names);
}
