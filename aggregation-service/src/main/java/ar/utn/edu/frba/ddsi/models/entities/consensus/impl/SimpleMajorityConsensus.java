package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;

public class SimpleMajorityConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.SIMPLE_MAJORITY;
  }


  @Override
  public boolean check(Event event, List<Source> sources){

    long halfOfTheSources = (long) Math.ceil(sources.size() / 2.0);

    long SourcesWithTheEvent = sources
        .parallelStream()
        .filter(s -> s.getEvents().parallelStream().anyMatch(e -> e.isEquivalent(event)))
        .count();

    return SourcesWithTheEvent >= halfOfTheSources;
  }
}
