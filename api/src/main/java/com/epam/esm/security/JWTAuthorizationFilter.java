package com.epam.esm.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final TokenUtil tokenUtil;

  public JWTAuthorizationFilter(AuthenticationManager authManager, TokenUtil tokenUtil) {
    super(authManager);
    this.tokenUtil = tokenUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String header = req.getHeader(HttpHeaders.AUTHORIZATION);

    if (header == null || !header.startsWith(tokenUtil.getToken_prefix())) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (token != null) {
      token = token.substring(7);
      return tokenUtil.getAuthentication(token);
    }
    logger.warn("JWT Token is not valid Bearer token");
    return null;
  }

}
