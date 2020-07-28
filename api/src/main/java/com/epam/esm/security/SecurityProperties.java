package com.epam.esm.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents security properties are being set in {@code .properties} files
 */
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
  /** secret using for signing JWT */
  private String secret = "SecretKeyToGenJWTs";
  /** Token type */
  private String tokenPrefix = "Bearer ";
  /** url to sign up new user */
  private String signUpUrl;
  /** token expiration time, default value 864_000_000L = 10 days */
  private long expirationTime = 864_000_000L;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getTokenPrefix() {
    return tokenPrefix;
  }

  public void setTokenPrefix(String tokenPrefix) {
    this.tokenPrefix = tokenPrefix;
  }

  public String getSignUpUrl() {
    return signUpUrl;
  }

  public void setSignUpUrl(String signUpUrl) {
    this.signUpUrl = signUpUrl;
  }

  public long getExpirationTime() {
    return expirationTime;
  }

  public void setExpirationTime(long expirationTime) {
    this.expirationTime = expirationTime;
  }
}
