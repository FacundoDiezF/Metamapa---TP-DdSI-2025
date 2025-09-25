package ar.utn.edu.frba.ddsi.models.entities.source;

import ar.utn.edu.frba.ddsi.converters.ImporterConverter;
import ar.utn.edu.frba.ddsi.models.dtos.input.EventImportDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Entity
@Table(name = "sources")
public class Source{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Convert(converter = ImporterConverter.class)
  private final IImporter importer;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private final Origin type;

  @Column(name = "path", nullable = false)
  private final String path;

  public Source(String path, IImporter importer) {
    this.path = path;
    this.importer = importer;
    this.type = Origin.STATIC;
  }

  public Set<EventImportDTO> importEvents() {
    return this.importer.importEvents(this);
  }
}