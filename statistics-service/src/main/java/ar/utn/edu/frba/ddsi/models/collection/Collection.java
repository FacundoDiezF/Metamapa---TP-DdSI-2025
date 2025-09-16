package ar.utn.edu.frba.ddsi.models.collection;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "collections")
public class Collection {

  @Id
  private String Id;

  @ManyToMany
  @JoinTable(
      name = "collection_events",
      joinColumns = @JoinColumn(name = "collection_id"),
      inverseJoinColumns = @JoinColumn(name = "event_id")
  )
  private List<Event> events = new ArrayList<>();
}
