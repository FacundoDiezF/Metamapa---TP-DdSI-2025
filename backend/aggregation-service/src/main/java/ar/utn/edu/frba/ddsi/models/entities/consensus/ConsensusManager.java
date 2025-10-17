package ar.utn.edu.frba.ddsi.models.entities.consensus;

import ar.utn.edu.frba.ddsi.models.entities.consensus.impl.AbsoluteConsensus;
import ar.utn.edu.frba.ddsi.models.entities.consensus.impl.MultipleMentionsConsensus;
import ar.utn.edu.frba.ddsi.models.entities.consensus.impl.SimpleMajorityConsensus;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsensusManager {

  private final ISourceRepository sourceRepository;
  private final IEventRepository eventRepository;

  private final List<IConsensusAlgorithm> consensusAlgorithms;

  @Autowired
  public ConsensusManager(IEventRepository eventRepository, ISourceRepository sourceRepository) {
    this.eventRepository = eventRepository;
    this.sourceRepository = sourceRepository;

    this.consensusAlgorithms = List.of(
        new MultipleMentionsConsensus(),
        new SimpleMajorityConsensus(),
        new AbsoluteConsensus()
    );
  }

  @Transactional
  public void runEvaluation() {

    List<Event> allEvents = eventRepository.findByDeleted(false);

    List<Source> allSources = sourceRepository.findAll();

    //TODO: Con jpa no es posible usar parallelStream, no permite la edicion de events en threaads paralelos.
    // Es posible ralizarlo devolviendo un DTO con el evnt y su nueva lista de consensos y luego actualizar los eventos en otro forEach fuera del parallel
    allEvents.forEach(event -> {
      List<ConsensusType> newConsensusList = new ArrayList<>();

      for (IConsensusAlgorithm consensus : consensusAlgorithms) {
        if (consensus.check(event, allSources)) {
          newConsensusList.add(consensus.getType());
        }
      }

      event.setPassedConsensus(newConsensusList);
    });

    eventRepository.saveAll(allEvents);
  }

}
