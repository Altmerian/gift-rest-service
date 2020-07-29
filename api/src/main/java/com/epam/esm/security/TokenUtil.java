package com.epam.esm.security;

import com.epam.esm.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to create and parse JWT authentication token
 */
@Component
public class TokenUtil {
  public static final String AUTHORITIES_KEY = "authorities";
  public static final String USER_ID_KEY = "userId";
  @Value("${security.expiration-time}")
  private long expirationTime;
  @Value("${security.secret}")
  private String secret;
  @Value("${security.token-prefix}")
  private String token_prefix;


  public String generateToken(Authentication auth) {
    AppUserDetails appUser = (AppUserDetails) auth.getPrincipal();
    Long userId = appUser.getUserId();
    String authorities =
        auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    return Jwts.builder()
        .setSubject(auth.getName())
        .claim(USER_ID_KEY, userId)
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(SignatureAlgorithm.HS512, secret.getBytes())
        .compact();
  }

  public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(secret.getBytes())
        .parseClaimsJws(token).getBody();
    String username;
    List<SimpleGrantedAuthority> authorities;
    Integer userId;
    try {
      username = claims.getSubject();
      authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      userId = (Integer) claims.get(USER_ID_KEY);
    } catch (NullPointerException exception) {
      throw new InvalidTokenException("Empty or invalid token!");
    }
    if (username != null) {
      return new UsernamePasswordAuthenticationToken(userId, username, authorities);
    }
    return null;
  }

  public long getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(long expirationTime) {
    this.expirationTime = expirationTime;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getToken_prefix() {
    return token_prefix;
  }

  public void setToken_prefix(String token_prefix) {
    this.token_prefix = token_prefix;
  }
}
