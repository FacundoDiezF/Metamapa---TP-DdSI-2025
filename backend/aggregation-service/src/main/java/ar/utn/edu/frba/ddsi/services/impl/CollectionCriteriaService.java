package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.repositories.ICollectionCriteriaRepository;
import ar.utn.edu.frba.ddsi.services.ICollectionCriteriaService;
import ar.utn.edu.frba.ddsi.services.IConditionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionCriteriaService implements ICollectionCriteriaService {

  private final ICollectionCriteriaRepository collectionCriteriaRepository;

  private final IConditionService conditionService;

  public CollectionCriteriaService(ICollectionCriteriaRepository collectionCriteriaRepository, IConditionService conditionService) {
    this.collectionCriteriaRepository = collectionCriteriaRepository;
    this.conditionService = conditionService;
  }

  @Override
  public CollectionCriteria findOrCreate(List<ConditionDTO> conditionDTOS) {
    //TODO: Validar si existe una CollectionCriteria con esas condiciones y en caso de que no exista crearla
    List<Condition> conditions = conditionService.findOrCreate(conditionDTOS);
    return collectionCriteriaRepository.save(new CollectionCriteria(conditions));
  }
}
