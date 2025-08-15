package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceClientInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceClientOutputDTO;
import java.util.List;

public interface ISourceClientService {

  SourceClientOutputDTO create(SourceClientInputDTO dto);

  List<SourceClientOutputDTO> getAllClients();
}
