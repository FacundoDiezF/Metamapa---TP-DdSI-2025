
import ar.edu.utn.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.edu.utn.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.edu.utn.frba.ddsi.models.entities.source.Source;
import ar.edu.utn.frba.ddsi.models.entities.source.SourceFactory;
import ar.edu.utn.frba.ddsi.models.repositories.ISourceRepository;
import ar.edu.utn.frba.ddsi.services.impl.EventService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.Set;

@RestController
@RequestMapping("/admin/imports")
public class CsvImportController {

    private final SourceFactory sourceFactory;
    private final ISourceRepository sourceRepository;
    private final EventService eventService;

    public CsvImportController(SourceFactory sourceFactory,
                               ISourceRepository sourceRepository,
                               EventService eventService) {
        this.sourceFactory = sourceFactory;
        this.sourceRepository = sourceRepository;
        this.eventService = eventService;
    }

    @PostMapping("/csv")
    public ResponseEntity<ImportResultDTO> importarCsv(@RequestParam("csvfile") MultipartFile csvfile) {
        if (csvfile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // 1) Carpeta local donde se guardan los CSV
            Path uploadsDir = Paths.get("uploads");
            Files.createDirectories(uploadsDir);

            // 2) Guardar archivo físico
            Path destino = uploadsDir.resolve(csvfile.getOriginalFilename());
            Files.copy(csvfile.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // 3) Armar DTO para la Source (usa tu factory)
            SourceInputDTO dto = SourceInputDTO.builder()
                    .fileName(destino.toString())  // esto va al field "path"
                    .importStrategy("CSV")         // matchea con CsvImporter.getType()
                    .build();

            // 4) Crear la Source y persistirla
            // ?(una source por CSV??)
            Source source = sourceFactory.createFrom(dto);
            source = sourceRepository.save(source);

            // 5) Ejecutar la importación
            Set<EventImportDTO> eventosImportados = source.importEvents();


            // 6) Persistir eventos con tu servicio/repositorio
            eventService.saveEvents(eventosImportados);


            // 7) Devolver resumen
            ImportResultDTO result = new ImportResultDTO(
                    source.getId(),
                    eventosImportados.size()
            );

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}
