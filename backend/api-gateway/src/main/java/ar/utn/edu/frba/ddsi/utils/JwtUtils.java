package ar.utn.edu.frba.ddsi.utils;

import ar.utn.edu.frba.ddsi.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

  private final JwtConfig jwtConfig;

  public JwtUtils(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public Claims getAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(jwtConfig.getPublicKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isInvalid(String token) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(jwtConfig.getPublicKey())
          .build()
          .parseClaimsJws(token); // Si fue manipulado o está expirado acá explota

      return false;
    } catch (Exception e) {
      return true;
    }
  }

  public boolean isAccessToken(String token) {
    try {
      Claims claims = getAllClaims(token);
      String type = claims.get("type", String.class);
      return "ACCESS".equals(type);
    } catch (Exception e) {
      return false;
    }
  }
}