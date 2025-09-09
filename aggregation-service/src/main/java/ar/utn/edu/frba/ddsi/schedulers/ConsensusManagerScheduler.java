package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.models.entities.consensus.ConsensusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConsensusManagerScheduler {

  private final ConsensusManager consensusManager;

  @Autowired
  public ConsensusManagerScheduler(ConsensusManager consensusManager) {
    this.consensusManager = consensusManager;
  }

  @Scheduled(cron = "${reevaluate.consensus.cron}")
  public void reevaluateEventsConsensus() {
    consensusManager.runEvaluation();
  }
}
