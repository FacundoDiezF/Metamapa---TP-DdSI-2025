package ar.utn.edu.frba.ddsi.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Getter
public class JwtConfig {

  @Value("${app.jwt.public-key}")
  private String publicKeyBase64;

  private RSAPublicKey publicKey;

  @PostConstruct
  public void init() {
    this.publicKey = decodePublicKey();
  }

  private RSAPublicKey decodePublicKey() {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) kf.generatePublic(spec);
    } catch (Exception e) {
      throw new RuntimeException("Error loading Public Key in Gateway", e);
    }
  }
}