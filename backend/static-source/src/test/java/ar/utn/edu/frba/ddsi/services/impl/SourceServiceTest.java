package ar.utn.edu.frba.ddsi.services.impl;

import ar.utn.edu.frba.ddsi.models.dtos.input.SourceInputDTO;
import ar.utn.edu.frba.ddsi.models.dtos.output.SourceOutputDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SourceServiceTest {

  @Autowired
  private SourceService sourceService;

  @Test
  @DisplayName("Al crear una fuente se guardan sus hechos.")
  public void testCreateSource() {

    // Arrange
    SourceInputDTO dto = new SourceInputDTO();
    dto.setFileName("sample_CSVtest_1.csv");
    dto.setImportStrategy("CSV");

    // Act
    SourceOutputDTO result = sourceService.create(dto);

    // Assert
    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getId());
  }

}
