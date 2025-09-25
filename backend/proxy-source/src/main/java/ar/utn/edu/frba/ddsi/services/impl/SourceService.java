package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.ISourceService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService implements ISourceService {

  @Autowired
  ISourceRepository sourceRepository;

  @Autowired
  Publisher publisher;

  @Override
  public void create(SourceInputDTO dto) {
    Source source = Source.from(dto);
    sourceRepository.save(source);
    publisher.notifyNewSource(source);
  }

  @Override
  public List<EventOutputDTO> getAllEventsBySourceId(Long id) {
    Source source = sourceRepository.findById(id);
    //TODO Return 404 if source not found
    return source.importEvents(null);
  }

  @Override
  public List<SourceOutputDTO> getAllSources() {
    return findAllSources().stream().map(SourceOutputDTO::from).toList();
  }

  @Override
  public List<Source> findAllSources() {
    return sourceRepository.findAll();
  }
}
