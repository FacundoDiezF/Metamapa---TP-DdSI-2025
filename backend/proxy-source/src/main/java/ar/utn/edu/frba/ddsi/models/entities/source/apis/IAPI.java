package ar.utn.edu.frba.ddsi.models.entities.source.apis;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface IAPI {
  List<EventOutputDTO> importEvents(LocalDateTime lastUpdate); //TODO: Verificar si esto no debería devolver Events y transformar a OutputDTO recién al final
}


