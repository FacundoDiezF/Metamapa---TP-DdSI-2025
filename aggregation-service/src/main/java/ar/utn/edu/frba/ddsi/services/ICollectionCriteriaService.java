package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.collection.ConditionDTO;
import ar.utn.edu.frba.ddsi.models.entities.collections.conditions.values.CollectionCriteria;

import java.util.List;

public interface ICollectionCriteriaService {
  CollectionCriteria findOrCreate(List<ConditionDTO> conditions);
}
