package interfaz.grafica.ddsi.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultimediaDTO {
  private String url;
  private String path;
  private String name;
  private String mimeType;
}
