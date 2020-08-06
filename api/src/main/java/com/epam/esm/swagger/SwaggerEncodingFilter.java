package com.epam.esm.swagger;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filters all requests to swagger-ui by checking whether the encoding matches the specified
 * encoding.
 */
@WebFilter(urlPatterns = {"/swagger-ui/**"})
@Component
public class SwaggerEncodingFilter implements Filter {
  private static final String ENCODING = "UTF-8";

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String requestEncoding = request.getCharacterEncoding();
    if (!ENCODING.equalsIgnoreCase(requestEncoding)) {
      request.setCharacterEncoding(ENCODING);
      response.setCharacterEncoding(ENCODING);
    }
    chain.doFilter(request, response);
  }
}
