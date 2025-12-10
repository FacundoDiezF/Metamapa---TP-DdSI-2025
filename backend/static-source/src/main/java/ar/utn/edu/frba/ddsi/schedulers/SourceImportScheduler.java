package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SourceImportScheduler {

    private final ISourceRepository sourceRepository;
    private final IEventService eventService;

    public SourceImportScheduler(ISourceRepository sourceRepository,
                                 IEventService eventService) {
        this.sourceRepository = sourceRepository;
        this.eventService = eventService;
    }

    @Scheduled(fixedDelayString = "${imports.fixed-delay-ms:60000}")
    public void importPendingSources() {


        List<Source> pendientes = sourceRepository.findByTypeAndImportedFalse(Origin.STATIC);

        for (Source source : pendientes) {
            try {
                Set<EventImportDTO> eventos = source.importEvents();
                eventService.saveEvents(eventos);

                source.setImported(true);
                sourceRepository.save(source);

               // publisher.notifyNewSource(sourceOutput);
                //eventos
            } catch (Exception e) {
                //TODO loggear error
            }
        }
    }
}
