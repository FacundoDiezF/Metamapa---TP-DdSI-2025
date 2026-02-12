package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;
import java.util.Map;

public class AbsoluteConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.ABSOLUTE;
  }

  @Override
  public boolean check(Event event, List<Source> sources, Map<Long, List<Event>> eventsBySourceId) {
    return sources.parallelStream()
        .allMatch(source -> {
          List<Event> sourceEvents = eventsBySourceId.get(source.getId());

          if (sourceEvents == null || sourceEvents.isEmpty()) {
            return false;
          }

          return sourceEvents.stream().anyMatch(e -> e.isEquivalent(event));
        });
  }
}
