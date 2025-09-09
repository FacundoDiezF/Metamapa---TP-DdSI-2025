package ar.utn.edu.frba.ddsi.models.repositories;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
  @Query("""
    SELECT c
    FROM Category c
    JOIN Event e ON e.category = c
    GROUP BY c.id
    ORDER BY COUNT(e.id) DESC
    LIMIT :count
  """)
  List<Category> findTopCategories(Integer count);
}