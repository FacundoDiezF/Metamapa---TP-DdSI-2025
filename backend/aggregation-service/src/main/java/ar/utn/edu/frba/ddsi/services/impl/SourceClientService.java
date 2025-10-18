package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.ConnectException;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceClientOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.SourceClient;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceClientRepository;
import ar.utn.edu.frba.ddsi.services.ISourceClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SourceClientService implements ISourceClientService {

  private final ISourceClientRepository sourceClientRepository;

  @Value("${aggregation-service.url}")
  String callbackUrl;

  @Autowired
  public SourceClientService(ISourceClientRepository sourceClientRepository) {
    this.sourceClientRepository = sourceClientRepository;
  }

  @Override
  public SourceClientOutputDTO create(SourceClientInputDTO dto) {
    SourceClient sourceClient = sourceClientRepository.save(SourceClient.from(dto));

    try {
      // Subscribe to the source client to receive NEW sources and events.
      sourceClient.subscribe(callbackUrl + "/source-clients/" + sourceClient.getId());
    } catch ( ConnectException e) {
      // If a connection-related exception occurs during subscription, we delete the source client to avoid having an unusable client.
      //sourceClientRepository.delete(sourceClient);
      //! Genera problemas si el error no es de conexion (osea si logra conetarse, crea alguna fuante y luego falla) ya que intenta borrar el cliente con sources asociadas y falla JPA.
      throw e;
    }

    return SourceClientOutputDTO.from(sourceClient);
  }

  @Override
  public List<SourceClientOutputDTO> getAllClients() {
    return sourceClientRepository.findAll().stream()
        .map(SourceClientOutputDTO::from)
        .toList();
  }
}
