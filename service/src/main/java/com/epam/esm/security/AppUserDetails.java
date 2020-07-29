package com.epam.esm.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AppUserDetails extends User {

  private Long userId;

  public AppUserDetails(String username, String password, Long userId, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.userId = userId;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }
}
