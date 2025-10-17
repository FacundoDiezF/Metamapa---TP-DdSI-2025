package ar.utn.edu.frba.ddsi.models.entities.source;


import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import java.util.Set;

public interface IImporter {

  Set<EventImportDTO> importEvents(Source source);

  String getType();
}