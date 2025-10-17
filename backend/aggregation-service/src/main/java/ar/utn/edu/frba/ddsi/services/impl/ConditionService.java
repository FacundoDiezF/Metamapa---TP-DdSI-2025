package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.CategoryCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.FromDateCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.TitleCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.impl.UntilDateCondition;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.repositories.ICategoryRepository;
import ar.utn.edu.frba.ddsi.models.repositories.IConditionRepository;
import ar.utn.edu.frba.ddsi.services.ICategoryService;
import ar.utn.edu.frba.ddsi.services.IConditionService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ConditionService implements IConditionService {

  private final IConditionRepository conditionRepository;

  private final ICategoryRepository categoryRepository;

  public ConditionService(IConditionRepository conditionRepository, ICategoryRepository categoryRepository) {
    this.conditionRepository = conditionRepository;
    this.categoryRepository = categoryRepository;
  }

  @Override
  public List<Condition> findOrCreate(List<ConditionDTO> conditionDTO) {
    //TODO: Validar que exista cada condicion y caso contrario crearla
    List<Condition> conditions = conditionDTO
        .stream()
        .map(this::factoryCondition)
        .toList();

    conditionRepository.saveAll(conditions);

    return conditions;
  }

  private Condition factoryCondition(ConditionDTO conditionDTO) {

    return switch (conditionDTO.getConditionType()) {
      case CATEGORY -> {
        Category category = categoryRepository.findById(Long.parseLong(conditionDTO.getConditionValue()))
            .orElseThrow(() -> new NotFoundException("Category not found with Id:" + conditionDTO.getConditionValue()));
        yield new CategoryCondition(category);
      }
      case TITLE -> new TitleCondition(conditionDTO.getConditionValue());
      case FROM_DATE -> new FromDateCondition(LocalDateTime.parse(conditionDTO.getConditionValue()));
      case TO_DATE -> new UntilDateCondition(LocalDateTime.parse(conditionDTO.getConditionValue()));

      //TODO: Manejar errores de LocalDateTime.parse y Long.parseLong
    };
  }
}
