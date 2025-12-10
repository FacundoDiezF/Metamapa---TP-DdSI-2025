package interfaz.grafica.ddsi.services;


import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Slf4j
@Service
public class SourcesService {

    private final ServerAdapter server;

    public SourcesService(ServerAdapter server) {this.server = server;}

    public List<SourceDTO> getAll() {return mockAllSources();}


    public void postNewSource(MultipartFile csvfile) {
        log.info("Y ACA TAMBIEN LLEGO");
        try {
            server.postNewSource(csvfile);
        } catch (WebClientResponseException e) {
            throw new BackendException(e);
        } catch (Exception e) {
            throw new ConnectionException("Could not connect to backend");
        }
    }









    private List<SourceDTO> mockAllSources() {
        return List.of(
                new SourceDTO(
                        1L,
                        101L,
                        Origin.STATIC
                ),
                new SourceDTO(
                        2L,
                        102L,
                        Origin.STATIC
                ),
                new SourceDTO(
                        3L,
                        103L,
                        Origin.STATIC
                ),
                new SourceDTO(
                        4L,
                        104L,
                        Origin.STATIC
                )

        );
    }

}
