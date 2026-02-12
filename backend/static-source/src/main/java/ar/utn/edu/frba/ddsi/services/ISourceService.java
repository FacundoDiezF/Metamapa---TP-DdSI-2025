package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ISourceService {
  SourceOutputDTO create(SourceInputDTO sourceInputDTO);

    SourceOutputDTO createFromCsvFile(MultipartFile csvfile) throws IOException;

    List<SourceOutputDTO> getSources();

    List<EventOutputDTO> getAllEventsBySourceId(Long id);

    void importSourceEvents(Long id);

    void importPendingSources();
}