package ar.utn.edu.frba.ddsi.services.impl;

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
      //Subscribe to the source client to receive NEW sources and events.
      sourceClient.subscribe(callbackUrl);
    } catch (Exception e) {
      // If subscription fails, remove the created source client to maintain consistency.
      //sourceClientRepository.delete(sourceClient); //! Está tirando un error cuando sucede esto porque todavía tiene sources que la referencian
      //La gracia de esto en realidad es atrapar un error si falla la suscripcion, pero termina agarrando el error al fallar la crearcion de eventos
      //TODO Solucionar error de source client con sources referenciándola intentando ser borrada
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
