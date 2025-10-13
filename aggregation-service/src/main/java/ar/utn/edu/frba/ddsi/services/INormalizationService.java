package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;

import java.util.List;

public interface INormalizationService {

  List<Event> eventLocationNormalization(List<Event> events);
}
