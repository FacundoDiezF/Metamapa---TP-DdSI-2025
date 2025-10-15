package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.PageDTO;
import interfaz.grafica.ddsi.dtos.ProvinceDTO;
import java.util.List;
import interfaz.grafica.ddsi.exceptions.BackendException;
import interfaz.grafica.ddsi.exceptions.ConnectionException;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class LocationService {

  private final ServerAdapter server;

  public LocationService(ServerAdapter server){
    this.server = server;
  }

  public PageDTO<ProvinceDTO> getAllProvinces(int page, int size) {
    //TODO: TEST && Revisar si size es necesario
    try {
      return server.getAllProvinces(page, size);
    } catch (WebClientResponseException e) {
      throw new BackendException("Backend error: " + e.getStatusCode());
    } catch (Exception e) {
      throw new ConnectionException("Could not connect to backend");
    }
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
