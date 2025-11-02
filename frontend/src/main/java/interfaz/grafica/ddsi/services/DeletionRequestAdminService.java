package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.*;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeletionRequestAdminService {

    public List<DeletionRequestDTO> getAll() {return mockAllDeletions();}

    private List<DeletionRequestDTO> mockAllDeletions() {
        return List.of(
                new DeletionRequestDTO(
                        new EventDTO(
                                1L,
                                10L,
                                Origin.PROXY,
                                "Inundación en Palermo",
                                "Fuertes lluvias causaron anegamientos",
                                "Inundación",
                                new LocationDTO(
                                        -34.571, -58.423,
                                        "Buenos Aires",
                                        "Comuna 14",
                                        "Palermo"
                                ),
                                LocalDate.of(2025, 1, 15)
                        ),
                        "El evento está duplicado con otro existente",
                        "Juan Pérez"
                ),
        new DeletionRequestDTO(
                new EventDTO(
                        2L,
                        11L,
                        Origin.PROXY,
                        "Incendio en Reserva Ecológica",
                        "Fuego controlado por bomberos",
                        "Incendio",
                        new LocationDTO(
                                -34.613, -58.351,
                                "Buenos Aires",
                                "Comuna 1",
                                "Puerto Madero"
                        ),
                        LocalDate.of(2025, 2, 20)
                ),
                "Información incorrecta",
                "Ana Gómez"
        ),
                new DeletionRequestDTO(
                        new EventDTO(
                                3L,
                                12L,
                                Origin.PROXY,
                                "Granizo en Caballito",
                                "Granizo del tamaño de una pelota de golf",
                                "Granizo",
                                new LocationDTO(
                                        -34.616, -58.443,
                                        "Buenos Aires",
                                        "Comuna 6",
                                        "Caballito"
                                ),
                                LocalDate.of(2025, 3, 5)
                        ),
                        "No corresponde con un hecho real",
                        "Carlos López"
                ),
                new DeletionRequestDTO(
                        new EventDTO(
                                4L,
                                13L,
                                Origin.PROXY,
                                "Vientos fuertes en Costanera",
                                "Ráfagas superiores a 80 km/h",
                                "Viento",
                                new LocationDTO(
                                        -34.605, -58.356,
                                        "Buenos Aires",
                                        "Comuna 1",
                                        "Costanera Sur"
                                ),
                                LocalDate.of(2025, 4, 2)
                        ),
                        "Reporte malintencionado",
                        "María Fernández"
                )
        );
    }


}
