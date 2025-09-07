package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;
import ar.utn.edu.frba.ddsi.models.repositories.IConditionRepository;
import ar.utn.edu.frba.ddsi.services.IConditionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService implements IConditionService {

  IConditionRepository conditionRepository;

  public ConditionService(IConditionRepository conditionRepository) {
    this.conditionRepository = conditionRepository;
  }

  @Override
  public List<Condition> findOrCreate(List<ConditionDTO> conditionDTO) {
    //TODO: Validar que exista cada condicion y caso contrario crearla
    List<Condition> conditions = conditionDTO
        .stream()
        .map(c -> c.getConditionType().toCondition(c.getConditionValue()))
        //.peek(conditionRepository::save)
        .toList();

    conditionRepository.saveAll(conditions);

    return conditions;
  }

}
