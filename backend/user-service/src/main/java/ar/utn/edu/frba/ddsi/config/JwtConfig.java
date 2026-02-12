package ar.utn.edu.frba.ddsi.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
@Getter
public class JwtConfig {

  @Value("${app.jwt.private-key}")
  private String privateKeyBase64;

  @Value("${app.jwt.public-key}")
  private String publicKeyBase64;

  @Value("${app.jwt.expiration-minutes}")
  private long expirationMinutes;

  @Value("${app.jwt.refresh-expiration-minutes}")
  private long refreshExpirationMinutes;

  private RSAPrivateKey privateKey;
  private RSAPublicKey publicKey;

  @PostConstruct
  public void init() {
    this.privateKey = decodePrivateKey();
    this.publicKey = decodePublicKey();
  }

  public RSAPrivateKey decodePrivateKey() {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPrivateKey) kf.generatePrivate(spec);
    } catch (Exception e) {
      throw new RuntimeException("Error loading Private Key from configuration", e);
    }
  }

  public RSAPublicKey decodePublicKey() {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(publicKeyBase64);
      X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) kf.generatePublic(spec);
    } catch (Exception e) {
      throw new RuntimeException("Error loading Public Key from configuration", e);
    }
  }
}
