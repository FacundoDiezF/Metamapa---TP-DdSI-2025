package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;
import java.util.Map;

public class SimpleMajorityConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.SIMPLE_MAJORITY;
  }


  @Override
  public boolean check(Event event, List<Source> allSources, Map<Long, List<Event>> eventsBySourceId) {

    long halfOfTheSources = (long) Math.ceil(allSources.size() / 2.0);

    long sourcesWithTheEvent = allSources
        .parallelStream()
        .filter(source -> {

          List<Event> sourceEvents = eventsBySourceId.get(source.getId());

          if (sourceEvents == null || sourceEvents.isEmpty()) {
            return false;
          }

          return sourceEvents.stream().anyMatch(e -> e.isEquivalent(event));
        })
        .count();

    return sourcesWithTheEvent >= halfOfTheSources;
  }
}
