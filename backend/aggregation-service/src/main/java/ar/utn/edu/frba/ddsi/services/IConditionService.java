package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.Condition;

import java.util.List;

public interface IConditionService {
  List<Condition> findOrCreate(List<ConditionDTO> conditions);
}
