package ar.utn.edu.frba.ddsi.models.entities.consensus.impl;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusType;
import ar.utn.edu.frba.ddsi.models.entities.consensus.IConsensusAlgorithm;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class MultipleMentionsConsensus implements IConsensusAlgorithm {
  @Override
  public ConsensusType getType() {
    return ConsensusType.MULTIPLE_MENTIONS;
  }

  @Override
  public boolean check(Event event, List<Source> sources, Map<Long, List<Event>> eventsBySourceId) {
    return itsInTwoSources(event, sources, eventsBySourceId) && !sameTitleDifferentAttributes(event, sources,eventsBySourceId);
  }

  private boolean itsInTwoSources(Event event, List<Source> sources, Map<Long, List<Event>> eventsBySourceId) {

    long SourcesWithTheEvent = sources
        .parallelStream()
        .filter(s -> {
          List<Event> eventsInSource = eventsBySourceId.get(s.getId());

          if (eventsInSource == null) {
            return false;
          }

          return eventsInSource.stream().anyMatch(e -> e.isEquivalent(event));
        })
        .count();

    return SourcesWithTheEvent >= 2;
  }

  private boolean sameTitleDifferentAttributes(Event event, List<Source> sources, Map<Long, List<Event>> eventsBySourceId) {
    return sources.stream()
        .flatMap(s -> {
          List<Event> sourceEvents = eventsBySourceId.get(s.getId());
          return sourceEvents != null ? sourceEvents.stream() : Stream.empty();
        })
        .anyMatch(e -> Objects.equals(e.getTitle(), event.getTitle())
            && !e.isEquivalent(event));
  }
}

