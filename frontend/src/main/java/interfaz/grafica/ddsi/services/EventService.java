package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.EventDTO;
import interfaz.grafica.ddsi.dtos.LocationDTO;
import interfaz.grafica.ddsi.dtos.Origin;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventService {

  public List<EventDTO> getEvents(){
    //TODO que le pida al agregador todos los eventos
    return mockEvents();
  }

  //! BORRAR CUANDO ESTE EL AGREGADOR
  private List<EventDTO> mockEvents() {
    return List.of(
        new EventDTO(
            1L,
            101L,
            Origin.STATIC,
            "Impacto de Estallido de gasoducto en comunidad...",
            "Un grave estallido de gasoducto se registró en la zona sur...",
            "Estallido de gasoducto",
            new LocationDTO(-31.742240, -60.536204, "Entre Ríos"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        new EventDTO(
            2L,
            102L,
            Origin.PROXY,
            "Explosión de tanque de gas provoca emergencia ...",
            "Grave explosión de tanque de gas ocurrió en la zona urbana...",
            "Explosión de tanque de gas",
            new LocationDTO(-45.560767, -69.084694, "Chubut"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        new EventDTO(
            3L,
            103L,
            Origin.DYNAMIC,
            "Equipos de emergencia atienden Incendio en vivienda...",
            "Autoridades confirmaron un incendio en vivienda familiar...",
            "Incendio en viviendas",
            new LocationDTO(-31.262872, -61.495656, "Santa Fe"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        new EventDTO(
            4L,
            104L,
            Origin.METAMAPA,
            "Consecuencias graves tras Escape de gas en El ...",
            "Grave escape de gas ocurrió en las inmediaciones del barrio...",
            "Escape de gas",
            new LocationDTO(-41.993731, -71.520418, "Río Negro"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        new EventDTO(
            5L,
            105L,
            Origin.STATIC,
            "Declaran emergencia tras Apagón por tormenta ...",
            "Un grave apagón por tormenta se registró en La Patagonia...",
            "Apagón por tormenta",
            new LocationDTO(-46.540843, -68.979251, "Santa Cruz"),
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now()
        )
    );
  }
}
