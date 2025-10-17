package ar.utn.edu.frba.ddsi.models.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageDTO<T> {

  private List<T> content;
  private int totalPages;
  private long totalElements;
  @JsonProperty("number")
  private int pageNumber;
  private boolean first;
  private boolean last;
}
