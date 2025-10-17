package ar.utn.edu.frba.ddsi.models.repositories.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Category;
import ar.utn.edu.frba.ddsi.models.entities.event.values.Origin;
import ar.utn.edu.frba.ddsi.models.entities.source.Source;
import ar.utn.edu.frba.ddsi.models.repositories.IEventRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventRepositoryTest {
  @Autowired
  private IEventRepository eventRepository;

  static LocalDateTime lastUpdate;

  @BeforeAll
  public static void init(@Autowired IEventRepository eventRepository) throws InterruptedException {
    var event1 = new Event(
        "Event 1",
        "Description 1",
        new Category("Category 1"),
        0.0,
        0.0,
        LocalDateTime.of(2023, 7, 27, 18, 15),
        Origin.STATIC,
        Mockito.mock(Source.class)
    );

    lastUpdate = LocalDateTime.now();

    Thread.sleep(1);

    var event2 = new Event(
        "Event 2",
        "Description 2",
        new Category("Category 2"),
        0.0,
        0.0,
        LocalDateTime.of(2022, 12, 12, 9, 58),
        Origin.STATIC,
        Mockito.mock(Source.class)
    );

    eventRepository.save(event1);
    eventRepository.save(event2);
  }

  @Test
  @DisplayName("Se obtienen hechos no eliminados")
  public void testFindEvents() {
    var event1 = eventRepository.findById(1L);
    var event2 = eventRepository.findById(2L);

    var result = eventRepository.findByDeleted(false);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(event1, result.get(0));
    assertEquals(event2, result.get(1));
  }

  @Test
  @DisplayName("Se elimina un hecho")
  public void testDeleteEvent() {
    var event1 = eventRepository.findById(1L);
    var event2 = eventRepository.findById(2L);
    var event3 = new Event(
        "Event 3",
        "Description 3",
        new Category("Category 3"),
        0.0,
        0.0,
        LocalDateTime.of(2024, 1, 1, 12, 0),
        Origin.STATIC,
        Mockito.mock(Source.class)
    );

    eventRepository.save(event3);

    var event = eventRepository.findById(3L);
    event.markAsDeleted();
    eventRepository.save(event);

    var events = eventRepository.findByDeleted(false);

    var deletedEvents = eventRepository.findByDeleted(true);

    assertNotNull(events);
    assertEquals(2, events.size());
    assertEquals(event1, events.get(0));
    assertEquals(event2, events.get(1));

    assertNotNull(deletedEvents);
    assertEquals(1, deletedEvents.size());
    assertEquals(event3, deletedEvents.get(0));
  }

}