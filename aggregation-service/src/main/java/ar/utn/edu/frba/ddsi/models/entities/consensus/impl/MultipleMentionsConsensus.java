package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;
import java.util.Objects;

public class MultipleMentionsConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.MULTIPLE_MENTIONS;
  }

  @Override
  public boolean check(Event event, List<Source> sources) {
    return itsInTwoSources(event, sources) && !sameTitleDifferentAttributes(event, sources);
  }

  private boolean itsInTwoSources(Event event, List<Source> sources) {

    long SourcesWithTheEvent = sources
        .parallelStream()
        .filter(s -> s.getEvents().parallelStream().anyMatch(e -> e.isEquivalent(event)))
        .count();

    return SourcesWithTheEvent >= 2;
  }

  private boolean sameTitleDifferentAttributes(Event event, List<Source> sources) {
    return sources.stream()
        .flatMap(s -> s.getEvents().stream())
        .anyMatch(e -> Objects.equals(e.getTitle(), event.getTitle())
            && !e.isEquivalent(event));
  }
}

