package com.epam.esm.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

/** Handles all exceptions that haven't reached {@link ControllerExceptionHandler} */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends GenericFilterBean {

  private final ObjectMapper objectMapper;
  private static final Logger LOG4J_LOGGER = LogManager.getLogger();

  @Autowired
  public ExceptionHandlerFilter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    try {
      chain.doFilter(req, res);
    } catch (RequestRejectedException exception) {
      LOG4J_LOGGER.error(
          "The request was rejected because the URL contained a potentially malicious String \"//\": request_url={}",
          request.getRequestURL(),
          exception);
      createErrorResponse(response, exception, HttpStatus.BAD_REQUEST);
    } catch (InvalidTokenException | JwtException | AppAuthenticationException exception) {
      LOG4J_LOGGER.error(exception.getMessage());
      String loginUrl = ControllerExceptionHandler.getLoginUrl(request);
      response.setHeader(HttpHeaders.WWW_AUTHENTICATE, loginUrl);
      createErrorResponse(response, exception, HttpStatus.UNAUTHORIZED);
    } catch (RuntimeException exception) {
      LOG4J_LOGGER.error(exception.getMessage());
      createErrorResponse(response, exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private void createErrorResponse(
      HttpServletResponse res, RuntimeException exception, HttpStatus status) throws IOException {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(status.value());
    errorResponse.setMessages(Collections.singletonList(exception.getMessage()));
    errorResponse.setTime(LocalDateTime.now().atZone(ZoneId.systemDefault()));

    res.setStatus(status.value());
    try (PrintWriter pw = res.getWriter()) {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(pw, errorResponse);
      pw.flush();
    }
  }
}
