package com.epam.esm.security;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Adds Access-Control-Expose-Headers header for CORS
 */
@WebFilter(urlPatterns = {"/**"})
@Component
public class CorsHeadersFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setHeader("Access-Control-Expose-Headers", "X-Total-Count, Location, Origin, X-Requested-With, Content-Type, Accept");
    chain.doFilter(request, response);
  }
}
