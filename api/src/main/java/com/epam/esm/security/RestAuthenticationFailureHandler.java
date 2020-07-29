package com.epam.esm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handles failure login efforts with corresponding response
 */
@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
  private final ObjectMapper objectMapper;

  @Autowired
  public RestAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse httpServletResponse,
      AuthenticationException ex)
      throws IOException {

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("status", 401);
    response.put("messages", new String[] {"authentication error", ex.getMessage()});
    response.put("time", ZonedDateTime.now());

    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    OutputStream out = httpServletResponse.getOutputStream();
    objectMapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
    out.flush();
  }
}
