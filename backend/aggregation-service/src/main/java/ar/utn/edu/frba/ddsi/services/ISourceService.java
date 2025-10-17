package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.source.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.source.SourceOutputDTO;
import java.util.List;

public interface ISourceService {

  SourceOutputDTO create(SourceInputDTO dto);

  List<SourceOutputDTO> getAllSources();
}
