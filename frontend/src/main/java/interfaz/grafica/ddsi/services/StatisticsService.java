package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    public List<StatisticsDTO> findTopCategories() {
      //TODO: GET /statistics/categories/top?count=4
      return  mockTopCategories();
    }

    public List<StatisticsDTO> findTopProvincesOnCollection() {
      //TODO: GET /statistics/collection/{handler}/provinces/top?count=4
      return  mockTopProvinces();
    }

    public List<StatisticsDTO> findTopProvincesByCategory() {
      // TODO: GET /statistics/category/{idCategory}/provinces/top?count=4
        return  mockTopProvinces();
    }

    public List<StatisticsDTO> findBusiestHoursByCategory() {
      //TODO: GET/statistics/category/{idCategory}/busiest-hours/top
        return  mockBusiestHours();
    }


    private List<StatisticsDTO> mockTopCategories() {
        return List.of(
                new StatisticsDTO(
                        "categoria1",
                        150L
                ),

                new StatisticsDTO(
                        "categoria2",
                        120L
                ),

                new StatisticsDTO(
                        "categoria3",
                        90L
                ),

                new StatisticsDTO(
                        "categoria4",
                        80L
                )
        );
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

    private List<StatisticsDTO> mockBusiestHours(){
        return List.of(
                new StatisticsDTO(
                        "4am",
                        222L
                ),

                new StatisticsDTO(
                        "2pm",
                        181L
                ),

                new StatisticsDTO(
                        "7pm",
                        160L
                ),

                new StatisticsDTO(
                        "6am",
                        140L
                )
        );
    }
}
