package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.Filter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder bCryptPasswordEncoder;
  private final AuthenticationFailureHandler authenticationFailureHandler;
  private final TokenUtil tokenUtil;

  @Value("${security.sign-up-url}")
  private String signUpUrl;

  public static final String[] DEFAULT_EXCLUDE_PATTERN =
      new String[] {"/api/v1/certificates/**", "/api/v1/tags/**", "/api-docs/**", "/swagger-.*"};

  @Autowired
  public WebSecurityConfig(
      @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService,
      PasswordEncoder bCryptPasswordEncoder,
      AuthenticationFailureHandler authenticationFailureHandler,
      TokenUtil tokenUtil) {
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.authenticationFailureHandler = authenticationFailureHandler;
    this.tokenUtil = tokenUtil;
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
        .addFilter(getUpJwtAuthenticationFilter())
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenUtil))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  private Filter getUpJwtAuthenticationFilter() throws Exception {
    JWTAuthenticationFilter jwtAuthenticationFilter =
        new JWTAuthenticationFilter(authenticationManager(), tokenUtil);
    jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
    return jwtAuthenticationFilter;
  }
}
