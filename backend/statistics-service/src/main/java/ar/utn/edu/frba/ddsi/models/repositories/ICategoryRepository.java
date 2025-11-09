package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

  Category findByName(String name);

  //TODO: Ver si esto anda bien
  @Query("""
        SELECT new ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO(c.name, COUNT(e.id))
        FROM Category c
        JOIN Event e ON e.category = c
        GROUP BY c.id
        ORDER BY COUNT(e.id) DESC
        LIMIT :count
      """)
  List<TopOutputDTO> findTopCategories(Integer count);


  //TODO: Ver si esto anda bien y crear si es necesario un DTO para el resultado
  @Query("""
      SELECT new ar.utn.edu.frba.ddsi.models.dtos.output.TopOutputDTO(
          HOUR(e.updatedDate),
          COUNT(e.id)
        )
        FROM Event e
        WHERE e.category.id = :categoryId
        GROUP BY HOUR(e.updatedDate)
        ORDER BY COUNT(e.id) DESC
        LIMIT :count
      """)
  List<TopOutputDTO> findCategoryBusiestHours(Long categoryId, Integer count);
}
