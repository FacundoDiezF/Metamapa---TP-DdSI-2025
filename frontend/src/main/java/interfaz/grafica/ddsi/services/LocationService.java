package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  public List<ProvinceDTO> getAllProvinces() {
    //TODO: hacer la peticion de provincias al agregador
    return mockProvinces();
  }

  private List<ProvinceDTO>  mockProvinces() {
    return List.of(
        new ProvinceDTO(1L, "Buenos Aires"),
        new ProvinceDTO(2L, "Catamarca"),
        new ProvinceDTO(3L, "Chaco"),
        new ProvinceDTO(4L, "Chubut"),
        new ProvinceDTO(5L, "Córdoba"),
        new ProvinceDTO(6L, "Corrientes"),
        new ProvinceDTO(7L, "Entre Ríos")
    );
  }

}
