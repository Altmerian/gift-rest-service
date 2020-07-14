package com.epam.esm.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TokenUtil {
  public static final String AUTHORITIES_KEY = "authorities";


  static String generateToken(Authentication auth) {
    String authorities =
        auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    return Jwts.builder()
        .setSubject(auth.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
        .compact();
  }

  static UsernamePasswordAuthenticationToken getAuthentication(String token) {

    Claims claims = Jwts.parser()
        .setSigningKey(SecurityConstants.SECRET.getBytes())
        .parseClaimsJws(token).getBody();
    String user = claims.getSubject();
    List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
    if (user != null) {
      return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }
    return null;
  }
}
