package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import interfaz.grafica.ddsi.models.entitites.adapters.ServerAdapter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StatisticsService {

  private final ServerAdapter server;

  public StatisticsService(ServerAdapter server) {
    this.server = server;
  }

  public List<StatisticsDTO> findTopCategories() {
    return server.getTopCategories();
    //TODO: manejar errores
  }

  public List<StatisticsDTO> findTopProvincesOnCollection() {
    //TODO: GET /statistics/collection/{handler}/provinces/top?count=4
    return mockTopProvinces();
  }

  public List<StatisticsDTO> findTopProvincesByCategory(Long idCategory) {
    return server.getTopProvincesByCategory(idCategory);
  }

  public List<StatisticsDTO> findBusiestHoursByCategory(Long idCategory) {
    return server.getBuisiestHoursByCategory(idCategory);
  }

  private List<StatisticsDTO> mockTopProvinces() {
    return List.of(
        new StatisticsDTO(
            "Buenos Aires",
            150L
        ),

        new StatisticsDTO(
            "Cordoba",
            120L
        ),

        new StatisticsDTO(
            "Santa Fe",
            90L
        ),

        new StatisticsDTO(
            "Peru",
            80L
        )
    );
  }

}
