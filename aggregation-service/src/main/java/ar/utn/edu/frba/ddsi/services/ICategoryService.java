package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import java.util.List;

public interface ICategoryService {

  Category findOrCreate(String categoryName);

  List<Category> getAll();
}
