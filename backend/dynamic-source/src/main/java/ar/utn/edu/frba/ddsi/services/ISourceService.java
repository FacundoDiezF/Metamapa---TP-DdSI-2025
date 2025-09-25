package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;

import java.util.List;

public interface ISourceService {
  List<SourceOutputDTO> getSources();

  List<EventOutputDTO> getAllEventsBySourceId(Long id);
}