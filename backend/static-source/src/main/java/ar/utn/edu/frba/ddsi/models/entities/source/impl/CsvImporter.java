package ar.utn.edu.frba.ddsi.models.entities.source.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.IImporter;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import com.opencsv.CSVReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class CsvImporter implements IImporter {

  public String getType() {
    return "CSV";
  }

  @Override
  public Set<EventImportDTO> importEvents(Source source) {

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("CSV/" + source.getPath());
    if (inputStream == null)
      throw new RuntimeException("Could not find the file: " + source.getPath());

    Set<EventImportDTO> eventsDto = new HashSet<>();

    try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

      reader.readNext();
      String[] row;

      // Read each row from the CSV and create EventImportDTO
      while ((row = reader.readNext()) != null) {

        String title = row[0];
        String description = row[1];
        String category = row[2];
        Double latitude = Double.parseDouble(row[3]);
        Double longitude = Double.parseDouble(row[4]);
        LocalDateTime eventDate = null;

        try {
          eventDate = LocalDate.parse(row[5], DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
        } catch (Exception e) {
          System.out.println("Invalid Date in row: " + Arrays.toString(row));
        }

        // Add event dto to imperted events
        EventImportDTO eventDto = new EventImportDTO(title, description, category, latitude, longitude, eventDate, source.getId());
        eventsDto.add(eventDto);
      }
    } catch (Exception e) {
      e.printStackTrace(); //TODO Cambiar esto por un método de logging más robusto
    }
    return eventsDto;
  }
}
