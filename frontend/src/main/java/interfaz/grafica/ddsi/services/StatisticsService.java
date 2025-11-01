package interfaz.grafica.ddsi.services;

import interfaz.grafica.ddsi.dtos.StatisticsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    public List<StatisticsDTO> findTopCategories() {
        return  mockTopCategories();
    }

    public List<StatisticsDTO> findTopProvincesOnCollection() {
        return  mockTopProvinces();
    }

    public List<StatisticsDTO> findTopProvincesByCategory() {
        return  mockTopProvinces();
    }

    public List<StatisticsDTO> findBusiestHoursByCategory() {
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
