package com.epam.esm.security;

/**
 * Response object representing JWT authentication token
 */
public class JWTResponse {
  private final String jwtToken;

  public JWTResponse(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getJwtToken() {
    return jwtToken;
  }
}
