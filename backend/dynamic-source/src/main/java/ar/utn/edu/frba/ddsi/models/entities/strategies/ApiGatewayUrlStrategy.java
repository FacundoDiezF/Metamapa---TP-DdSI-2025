package ar.utn.edu.frba.ddsi.models.entities.strategies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiGatewayUrlStrategy implements UrlGenerationStrategy{
  @Value("${api.gateway.url}")
  private String apiGatewayUrl;

  @Value("${multimedia.path}")
  private String multimediaPath;

  @Override
  public String generateUrl(String resourcePath) {
    return apiGatewayUrl + multimediaPath + "/" + resourcePath;
  }
}
