package ar.utn.edu.frba.ddsi.models.entities.collection;

import ar.utn.edu.frba.ddsi.models.entities.event.Event;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collections")
public class Collection {

  @Id
  @Column(name = "handler")
  private String handler;

  @ManyToMany
  @JoinTable(
      name = "collection_events",
      joinColumns = @JoinColumn(name = "collection_id"),
      inverseJoinColumns = @JoinColumn(name = "event_id")
  )
  private List<Event> events = new ArrayList<>();

}
