package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceClientOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.impl.SourceClient;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceClientRepository;
import ar.utn.edu.frba.ddsi.services.ISourceClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SourceClientService implements ISourceClientService {

  ISourceClientRepository sourceClientRepository;

  @Value("${aggregation-service.url}")
  String callbackUrl;

  @Autowired
  public SourceClientService(ISourceClientRepository sourceClientRepository) {
    this.sourceClientRepository = sourceClientRepository;
  }

  @Override
  public SourceClientOutputDTO create(SourceClientInputDTO dto) {
    SourceClient sourceClient = sourceClientRepository.save(SourceClient.from(dto));

    //Subscribe to the source client to receive NEW sources and events.
    sourceClient.subscribe(callbackUrl);

    return SourceClientOutputDTO.from(sourceClient);
  }

  @Override
  public List<SourceClientOutputDTO> getAllClients() {
    return sourceClientRepository.getAllClients().stream()
        .map(SourceClientOutputDTO::from)
        .toList();
  }
}
