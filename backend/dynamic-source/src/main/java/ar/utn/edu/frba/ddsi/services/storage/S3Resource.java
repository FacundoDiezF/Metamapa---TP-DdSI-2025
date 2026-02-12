package ar.utn.edu.frba.ddsi.services.storage;

import lombok.Builder;
import lombok.Getter;
import java.io.InputStream;

@Getter
@Builder
public class S3Resource {
    private InputStream inputStream;
    private String contentType;
    private String filename;
    private long contentLength;
}