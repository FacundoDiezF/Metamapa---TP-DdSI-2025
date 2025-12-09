package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourcesService {

    public List<SourceDTO> getAll() {return mockAllSources();}

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
