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

  public static PageDTO nullPage() {
    PageDTO page = new PageDTO<>();
    page.setContent(List.of());
    page.setTotalPages(0);
    page.setTotalElements(0);
    page.setPageNumber(0);
    page.setFirst(true);
    page.setLast(true);
    return page;
  }
}
