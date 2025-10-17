package interfaz.grafica.ddsi.dtos;

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

  public PageDTO(List<T> content, int totalPages, long totalElements, int pageNumber, boolean first, boolean last) {
    this.content = content;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
    this.pageNumber = pageNumber;
    this.first = first;
    this.last = last;
  }
}
