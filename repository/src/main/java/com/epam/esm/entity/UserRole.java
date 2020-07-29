package com.epam.esm.entity;

public enum UserRole {
  GUEST("ROLE_GUEST"),
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  UserRole(String authority) {
    this.authority = authority;
  }

  private String authority;

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }
}
