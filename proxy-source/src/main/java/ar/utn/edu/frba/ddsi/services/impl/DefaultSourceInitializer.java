package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.entities.source.SourceFactory;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class DefaultSourceInitializer {

  private final SourceFactory sourceFactory;
  private final ISourceRepository sourceRepository;

  public DefaultSourceInitializer(SourceFactory sourceFactory, ISourceRepository sourceRepository) {
    this.sourceFactory = sourceFactory;
    this.sourceRepository = sourceRepository;
  }

  @PostConstruct
  public void initializeDefaultSources() {
    sourceRepository.save(sourceFactory.naturalDisaster());
  }
}
