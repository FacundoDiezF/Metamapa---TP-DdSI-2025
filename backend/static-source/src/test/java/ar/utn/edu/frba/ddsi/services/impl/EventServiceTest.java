package ar.utn.edu.frba.ddsi.services.impl;


import static org.mockito.Mockito.when;

import ar.utn.edu.frba.ddsi.models.dtos.output.EventOutputDTO;
import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class EventServiceTest {

  @Autowired
  private EventService eventService;

  @MockitoBean
  private IEventRepository mockEventRepository;

  Event event0 = new Event(
      "Event 0",
      "Description 0",
      new Category("Category 0"),
      0.0,
      0.0,
      LocalDateTime.of(2023, 7, 27, 18, 15),
      Origin.STATIC,
      Mockito.mock(Source.class)
  );
  Event event1 = new Event(
      "Event 1",
      "Description 1",
      new Category("Category 1"),
      0.0,
      0.0,
      LocalDateTime.of(2022, 12, 12, 9, 58),
      Origin.STATIC,
      Mockito.mock(Source.class)
  );

  @Test
  @DisplayName("Al pedir los hechos, se obtienen los no eliminados")
  public void testGetEvents() {
    when(mockEventRepository.findByDeleted(false)).thenReturn(List.of(event0, event1));

    var result = eventService.getEvents();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(EventOutputDTO.from(event0), result.get(0));
    Assertions.assertEquals(EventOutputDTO.from(event1), result.get(1));
  }
}