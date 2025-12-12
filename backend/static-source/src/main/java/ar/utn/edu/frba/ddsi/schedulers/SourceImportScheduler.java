package ar.utn.edu.frba.ddsi.schedulers;

import ar.utn.edu.frba.ddsi.mappers.SourceMapper;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.observer.Publisher;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.ISourceRepository;
import ar.utn.edu.frba.ddsi.services.IEventService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SourceImportScheduler {

    private final ISourceRepository sourceRepository;
    private final IEventService eventService;
    private final Publisher publisher;
    private final SourceMapper sourceMapper;

    public SourceImportScheduler(ISourceRepository sourceRepository,
                                 IEventService eventService,
                                 Publisher publisher,
                                 SourceMapper sourceMapper) {

        this.sourceRepository = sourceRepository;
        this.eventService = eventService;
        this.publisher = publisher;
        this.sourceMapper = sourceMapper;
    }

    @Scheduled(fixedDelayString = "${imports.fixed-delay-ms:10000}")
    public void importPendingSources() {


        System.out.println("[CRON] Ejecutando importPendingSources...");

        List<Source> pendientes = sourceRepository.findByTypeAndImportedFalse(Origin.STATIC);

        for (Source source : pendientes) {
            try {
                Set<EventImportDTO> eventosAImportar = source.importEvents();

                List<EventOutputDTO> eventsOut = new ArrayList<>();

                for(EventImportDTO evento : eventosAImportar){
                    EventOutputDTO eventDTO = eventService.createEvent(evento);
                    eventsOut.add(eventDTO);
                }


                source.setImported(true);
                sourceRepository.save(source);


                SourceOutputDTO sourceOut = sourceMapper.transformarASourceOutPut(source);
                publisher.notifyNewSource(sourceOut);
                publisher.notifyNewEvents(eventsOut);




            } catch (Exception e) {
                //TODO loggear error
            }
        }
    }
}
