package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;

public class AbsoluteConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.ABSOLUTE;
  }

  @Override
  public boolean check(Event event, List<Source> sources) {
    return sources.parallelStream()
        .allMatch(source -> source.getEvents().parallelStream().anyMatch(e -> e.isEquivalent(event)));
  }
}
