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
}