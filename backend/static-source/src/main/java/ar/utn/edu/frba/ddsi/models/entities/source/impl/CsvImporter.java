package ar.utn.edu.frba.ddsi.models.entities.source.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.entities.source.IImporter;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CsvImporter implements IImporter {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public Set<EventImportDTO> importEvents(Source source) {
        Set<EventImportDTO> events = new LinkedHashSet<>();

        Path path = Paths.get(source.getPath());

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                // Saltar header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] columns = parseCsvLine(line);

                // Esperamos: title, description, name, latitude, longitude, EventDate
                if (columns.length < 6) {
                    // Podés loguear que la línea es inválida si querés
                    continue;
                }

                String title = stripQuotes(columns[0]);
                String description = stripQuotes(columns[1]);
                String category = stripQuotes(columns[2]);
                double latitude = Double.parseDouble(stripQuotes(columns[3]));
                double longitude = Double.parseDouble(stripQuotes(columns[4]));
                String rawDate = stripQuotes(columns[5]);

                LocalDate eventDate = LocalDate.parse(rawDate, DATE_FORMATTER);
                LocalDateTime eventDate = date.atStartOfDay();

                Long sourceId = source.getId();

                EventImportDTO dto = new EventImportDTO(title, description, category, latitude, longitude, eventDate, sourceId);
                events.add(dto);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error leyendo CSV en path: " + path.toAbsolutePath(), e);
        }
        return events;
    }

    @Override
    public String getType() {
        return "CSV";
    }

    /**
     * Parser sencillo de una línea de CSV:
     * - respeta comas dentro de comillas
     * - no usa librerías externas
     */
    private String[] parseCsvLine(String line) {
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        java.util.List<String> result = new java.util.ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // alterna dentro/fuera de comillas
            } else if (c == ',' && !inQuotes) {
                // fin de campo
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }

        // último campo
        result.add(current.toString());

        return result.toArray(new String[0]);
    }

    private String stripQuotes(String value) {
        if (value == null) {
            return null;
        }
        value = value.trim();
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
