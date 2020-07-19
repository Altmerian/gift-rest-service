package com.epam.esm.security;

import com.epam.esm.exception.InvalidTokenException;
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
  public static final String USER_ID = "userId";


  static String generateToken(Authentication auth) {
    AppUserDetails appUser = (AppUserDetails) auth.getPrincipal();
    Long userId = appUser.getUserId();
    String authorities =
        auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
    return Jwts.builder()
        .setSubject(auth.getName())
        .claim(USER_ID, userId)
        .claim(AUTHORITIES_KEY, authorities)
        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
        .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
        .compact();
  }

  static UsernamePasswordAuthenticationToken getAuthentication(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(SecurityConstants.SECRET.getBytes())
        .parseClaimsJws(token).getBody();
    String username;
    List<SimpleGrantedAuthority> authorities;
    Integer userId;
    try {
      username = claims.getSubject();
      authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      userId = (Integer) claims.get(USER_ID);
    } catch (NullPointerException exception) {
      throw new InvalidTokenException("Empty or invalid token!");
    }
    if (username != null) {
      return new UsernamePasswordAuthenticationToken(userId, username, authorities);
    }
    return null;
  }
}
