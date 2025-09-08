package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.exceptions.NotFoundException;
import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.entities.source.SourceClient;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceClientRepository;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

  private final ISourceClientRepository sourceClientRepository;

  private final ISourceRepository sourceRepository;

  @Autowired
  public SourceService(ISourceClientRepository sourceClientRepository, ISourceRepository sourceRepository) {
    this.sourceClientRepository = sourceClientRepository;
    this.sourceRepository = sourceRepository;
  }

  @Override
  public SourceOutputDTO create(SourceInputDTO dto) {

    // TODO: Validar que la peticion venga de un modulo-fuente registrado (SEGURIDAD)

    SourceClient sourceClient = sourceClientRepository.findById(dto.getSourceClientId())
        .orElseThrow(() -> new NotFoundException("Source not found - ID: " + dto.getSourceClientId()));

    Source source = Source.builder()
        .sourceClient(sourceClient)
        .originSourceId(dto.getInClientId())
        .type(dto.getType())
        .build();

    sourceRepository.save(source);
    return SourceOutputDTO.from(source);
  }

  @Override
  public List<SourceOutputDTO> getAllSources() {
    return sourceRepository.findAll().stream()
        .map(SourceOutputDTO::from)
        .toList();
  }
}


