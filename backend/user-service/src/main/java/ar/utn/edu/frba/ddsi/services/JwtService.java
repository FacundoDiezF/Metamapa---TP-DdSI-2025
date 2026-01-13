package ar.utn.edu.frba.ddsi.services;

import ar.utn.edu.frba.ddsi.config.JwtConfig;
import ar.utn.edu.frba.ddsi.models.dtos.AuthResponseDTO;
import ar.utn.edu.frba.ddsi.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private final JwtConfig jwtConfig;

  public JwtService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  // ==========================================
  // GENERACIÓN DE TOKENS
  // ==========================================

  public AuthResponseDTO generateTokens(User user) {
    return AuthResponseDTO.builder()
        .accessToken(generateAccessToken(user))
        .refreshToken(generateRefreshToken(user))
        .build();
  }

  public String generateAccessToken(User user) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("roles", user.getRoles());
    extraClaims.put("userId", user.getPublicId());
    extraClaims.put("type", "ACCESS");

    return buildToken(extraClaims, user.getUsername(), jwtConfig.getExpirationMinutes());
  }

  public String generateRefreshToken(User user) {
    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("type", "REFRESH");

    return buildToken(extraClaims, user.getUsername(), jwtConfig.getRefreshExpirationMinutes());
  }

  private String buildToken(Map<String, Object> extraClaims, String subject, long expirationMinutes) {
    long expirationMillis = expirationMinutes * 60 * 1000;

    return Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
        .signWith(jwtConfig.getPrivateKey(), SignatureAlgorithm.RS256)
        .compact();
  }

  // ==========================================
  // VALIDACIÓN
  // ==========================================

  public boolean isRefreshTokenValid(String token) {
    try {

      if (isTokenExpired(token)) return false;

      String type = extractClaim(token, claims -> claims.get("type", String.class)); //* Acá explotaría si su firma no es válida
      return "REFRESH".equals(type);

    } catch (Exception e) {
      return false;
    }
  }

  // ==========================================
  // UTILIDADES DE EXTRACCIÓN
  // ==========================================

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(jwtConfig.getPublicKey())
        .build()
        .parseClaimsJws(token)// Acá explotaría si no es válido
        .getBody();
  }
}