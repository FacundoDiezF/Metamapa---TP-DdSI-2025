package ar.utn.edu.frba.ddsi.models.entities.consensus;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.util.List;

public interface IConsensusAlgorithm {

  ConsensusType getType();

  boolean check(Event event, List<Source> sources);
}
