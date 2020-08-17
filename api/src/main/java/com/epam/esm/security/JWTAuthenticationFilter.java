package com.epam.esm.security;

import com.epam.esm.dto.UserDetailsDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Main authentication security filter which checks user's credentials and if success returns Java
 * Web Token with claims
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final AuthenticationManager authenticationManager;
  private final TokenUtil tokenUtil;
  private static final Logger LOG4J_LOGGER = LogManager.getLogger();

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager, TokenUtil tokenUtil) {
    this.authenticationManager = authenticationManager;
    this.tokenUtil = tokenUtil;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
    UserDetailsDTO userDetails = new UserDetailsDTO();
    if (StringUtils.isBlank(req.getParameter("email"))) {
      try {
        userDetails = new ObjectMapper().readValue(req.getInputStream(), UserDetailsDTO.class);
      } catch (IOException e) {
        throw LOG4J_LOGGER.throwing(Level.ERROR, new RuntimeException(e));
      }
    } else {
      userDetails.setEmail(req.getParameter("email"));
      userDetails.setPassword(req.getParameter("password"));
    }
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(userDetails.getEmail(), userDetails.getPassword()));
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
