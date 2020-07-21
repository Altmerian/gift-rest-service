package com.epam.esm.security;

import com.epam.esm.dto.UserDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final TokenUtil tokenUtil;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, TokenUtil tokenUtil) {
    this.authenticationManager = authenticationManager;
    this.tokenUtil = tokenUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    try {
      UserDetailsDTO userDetails =
          new ObjectMapper().readValue(req.getInputStream(), UserDetailsDTO.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              userDetails.getEmail(), userDetails.getPassword()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
      throws IOException {
    String token = tokenUtil.generateToken(auth);
    JWTResponse jwtResponse = new JWTResponse(token);
    ObjectMapper objectMapper = new ObjectMapper();
    try (PrintWriter pw = res.getWriter()) {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(pw, jwtResponse);
      pw.flush();
    }
  }
}
