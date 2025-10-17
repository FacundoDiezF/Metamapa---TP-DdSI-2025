package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.CollectionDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.SourceDTO;
import java.util.List;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CollectionService {

  private final ServerAdapter server;

  public CollectionService(ServerAdapter server) {
    this.server = server;
  }

  public List<CollectionDTO> getTop() {
    //TODO: Que le pida al agregador 4 collecciones
    return mockColecciones().subList(0, 4);
  }

  public PageDTO<CollectionDTO> getAll(int page) {
    //TODO TEST
    try {
      return server.getAllCollections(page);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
  }

  public CollectionDTO getByHandler(String handler) {
    //TODO que le pida al agregador la coleccion por handler
    return mockColecciones().get(0);
  }

  //! BORRAR CUANDO ESTE EL AGREGADOR
  private List<CollectionDTO> mockColecciones() {
    return List.of(
        new CollectionDTO(
            "handler_static_1",
            "Colección de Mapas Estáticos",
            "Eventos naturales generados a partir de datos estáticos",
            List.of(
                new SourceDTO(1L, 100L, Origin.STATIC),
                new SourceDTO(2L, 101L, Origin.STATIC)
            ),
            List.of(101L, 102L, 103L)
        ),
        new CollectionDTO(
            "handler_proxy_2",
            "Colección de Eventos Proxificados",
            "Datos provenientes de un sistema intermedio (proxy)",
            List.of(
                new SourceDTO(3L, 200L, Origin.PROXY)
            ),
            List.of(201L, 202L)
        ),
        new CollectionDTO(
            "handler_dynamic_3",
            "Colección de Datos Dinámicos",
            "Eventos con actualización en tiempo real",
            List.of(
                new SourceDTO(4L, 300L, Origin.DYNAMIC),
                new SourceDTO(5L, 301L, Origin.PROXY)
            ),
            List.of(301L, 302L, 303L, 304L)
        ),
        new CollectionDTO(
            "handler_metamapa_4",
            "Colección de Metamapas",
            "Agrupaciones avanzadas de fuentes dinámicas y estáticas",
            List.of(
                new SourceDTO(6L, 400L, Origin.METAMAPA),
                new SourceDTO(7L, 401L, Origin.DYNAMIC)
            ),
            List.of(401L, 402L)
        ),
        new CollectionDTO(
            "handler_mixed_5",
            "Colección Mixta de Orígenes",
            "Conjunto combinado de fuentes de distintos tipos",
            List.of(
                new SourceDTO(8L, 500L, Origin.STATIC),
                new SourceDTO(9L, 501L, Origin.METAMAPA),
                new SourceDTO(10L, 502L, Origin.PROXY)
            ),
            List.of(501L, 502L, 503L)
        )
    );
  }
}
