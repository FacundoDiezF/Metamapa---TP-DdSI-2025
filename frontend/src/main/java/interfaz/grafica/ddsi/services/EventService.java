package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.LocationDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.output.EventOutputDTO;
import java.time.LocalDate;
import java.util.List;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;

@Service
public class EventService {


  private final ServerAdapter server;

  public EventService(ServerAdapter server){
    this.server = server;
  }

  public PageDTO<EventDTO> getAll(List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in) {
    //TODO TEST
    //!No está paginado
    try {
      return server.getAllEvents();
    } catch (Exception e) { //TODO Vista de error de conexión

      System.err.println("Error al conectar con el backend: " + e.getMessage());

      return new PageDTO<>();
    }
  }

  public List<EventDTO> getAllByCollection(String handler, List<Long> category_in, List<Long> province_in, LocalDate eventDate_lt, LocalDate eventDate_gt, List<Origin> source_in) {
    //TODO TEST
    //!No está paginado
    try {
      return server.getAllEventsByCollection(handler);
    } catch (Exception e) { //TODO Vista de error de conexión

      System.err.println("Error al conectar con el backend: " + e.getMessage());
      return List.of();
    }

  }

  public void postNew(EventOutputDTO newEvent) {
    //TODO que le mande el evento al agregador (Debemos crear el endpoint y que rediriga a la fuente dinamica)
  }

  //! BORRAR CUANDO ESTE EL AGREGADOR
  private List<EventDTO> mockEvents() {

    // Fechas base para los mocks
    LocalDate date1 = LocalDate.of(2025, 5, 5);
    LocalDate date2 = LocalDate.of(2025, 5, 6);
    LocalDate date3 = LocalDate.of(2025, 9, 15);
    LocalDate date4 = LocalDate.of(2025, 10, 10);
    LocalDate date5 = LocalDate.of(2025, 10, 11);
    LocalDate date6 = LocalDate.of(2025, 10, 12);
    LocalDate date7 = LocalDate.of(2025, 10, 13);

    return List.of(
        // Hecho 1 (Replicando la maqueta)
        new EventDTO(
            1L,
            101L,
            Origin.STATIC,
            "Impacto de Estallido de gasoducto en comunidad de Paraná, Entre Ríos",
            "Un grave estallido de gasoducto se registró en Paraná, Entre Ríos. El incidente obligó a evacuar a residentes de la zona. Se ha iniciado una investigación para determinar las causas exactas del incidente.",
            "Estallido de gasoducto",
            new LocationDTO(-31.742240, -60.536204, "Entre Ríos", "Paraná", "Paraná"),
            date1
        ),

        // Hecho 2 (Replicando la maqueta)
        new EventDTO(
            2L,
            102L,
            Origin.STATIC,
            "Otro hecho de ejemplo para probar el scroll interno",
            "Descripción del hecho para completar el feed y probar el scroll interno de la columna central. Esta descripción es corta.",
            "Estallido de gasoducto",
            new LocationDTO(-31.742240, -60.536204, "Entre Ríos", "Paraná", "San Benito"),
            date2
        ),

        // Hecho 3 (Inventado 1)
        new EventDTO(
            3L,
            103L,
            Origin.DYNAMIC,
            "Alerta Roja por Desborde de Río en Villa María",
            "Se declaró alerta roja debido a las fuertes lluvias que causaron el desborde del río en la madrugada, afectando zonas ribereñas.",
            "Desastre Natural",
            new LocationDTO(-32.407778, -63.243611, "Córdoba", "General San Martín", "Villa María"),
            date3
        ),

        // Hecho 4 (Inventado 2)
        new EventDTO(
            4L,
            104L,
            Origin.METAMAPA,
            "Corte Total en Ruta Nacional 3 por protestas sociales",
            "Manifestantes cortaron la Ruta 3 en reclamo de mejoras laborales y salariales. Se recomienda evitar la zona.",
            "Protesta Social",
            new LocationDTO(-45.8641, -67.4812, "Chubut", "Escalante", "Comodoro Rivadavia"),
            date4
        ),

        // Hecho 5 (Inventado 3)
        new EventDTO(
            5L,
            105L,
            Origin.PROXY,
            "Sismo de magnitud 5.1 cerca de la Cordillera",
            "Se registró un temblor de magnitud moderada con epicentro a 50 km al oeste de la capital mendocina, sin daños mayores reportados.",
            "Desastre Natural",
            new LocationDTO(-33.00, -68.82, "Mendoza", "Luján de Cuyo", "Potrerillos"),
            date5
        ),

        // Hecho 6 (Inventado 4)
        new EventDTO(
            6L,
            106L,
            Origin.STATIC,
            "Contaminación del Riachuelo por derrame químico",
            "Organizaciones ambientales reportan un nuevo derrame químico en el Riachuelo, afectando gravemente el ecosistema local.",
            "Medio Ambiente",
            new LocationDTO(-34.64, -58.37, "Buenos Aires", "Avellaneda", "Dock Sud"),
            date6
        ),

        // Hecho 7 (Inventado 5)
        new EventDTO(
            7L,
            107L,
            Origin.DYNAMIC,
            "Evacuación masiva por Incendio Forestal en Sierra de la Ventana",
            "Un incendio fuera de control en la zona serrana obligó a la evacuación preventiva de varias localidades turísticas.",
            "Incendio Forestal",
            new LocationDTO(-38.08, -61.94, "Buenos Aires", "Tornquist", "Sierra de la Ventana"),
            date7
        )
    );

  }
}
