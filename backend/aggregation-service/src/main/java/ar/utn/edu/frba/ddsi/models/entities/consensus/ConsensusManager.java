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
import java.util.Map;
import java.util.stream.Collectors;
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

    // 1. CARGA DE DATOS
    List<Event> allEvents = eventRepository.findByDeletedWithRelations(false);
    List<Source> allSources = sourceRepository.findAll();

    // 2. PRE-PROCESAMIENTO (Agrupamos los eventos por SourceId)
    Map<Long, List<Event>> eventsBySourceId = allEvents.stream()
        .collect(Collectors.groupingBy(e -> e.getSource().getId()));

    // 3. PROCESAMIENTO PARALELO
    Map<Event, List<ConsensusType>> updatesToApply = allEvents.parallelStream().collect(Collectors.toMap(
        event -> event,
        event -> {
          List<ConsensusType> newConsensusList = new ArrayList<>();
          for (IConsensusAlgorithm consensus : consensusAlgorithms) {
            if (consensus.check(event, allSources, eventsBySourceId)) {
              newConsensusList.add(consensus.getType());
            }
          }
          return newConsensusList;
        }
    ));

    // 4. ACTUALIZAMOS EL BATCH Y GUARDAMOS
    updatesToApply.forEach(Event::setPassedConsensus);
    eventRepository.saveAll(allEvents);
  }

}
