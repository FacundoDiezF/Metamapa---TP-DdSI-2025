package ar.utn.edu.frba.ddsi.controllers;

import ar.utn.edu.frba.ddsi.services.IMultimediaService;
import ar.utn.edu.frba.ddsi.services.storage.S3Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

  private final IMultimediaService multimediaService;

  @Autowired
  public MultimediaController(IMultimediaService multimediaService) {
    this.multimediaService = multimediaService;
  }

  @GetMapping("/**")
  public ResponseEntity<InputStreamResource> getMultimedia(HttpServletRequest request) {

    String path = extractPath(request);

    S3Resource resource = multimediaService.getMultimediaByPath(path);

    return buildStreamingResponse(resource);
  }


  private String extractPath(HttpServletRequest request) {
    String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
    String bestMatchingPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

    if (fullPath == null || bestMatchingPattern == null) {
      throw new IllegalArgumentException("No se pudo extraer el path de la URL");
    }

    return new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, fullPath);
  }

  private ResponseEntity<InputStreamResource> buildStreamingResponse(S3Resource resource) {
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(resource.getContentType()))
        .contentLength(resource.getContentLength())
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
        .body(new InputStreamResource(resource.getInputStream()));
  }
}