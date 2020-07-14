package com.epam.esm.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler
{
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                      AuthenticationException ex) throws IOException, ServletException
  {

    Map<String,Object> response = new HashMap<>();
    response.put("error","authentication error");
    response.put("message", ex.getMessage());

    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    OutputStream out = httpServletResponse.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
    out.flush();
  }
}
