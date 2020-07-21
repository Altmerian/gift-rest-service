package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

//@Configuration
//@EnableAuthorizationServer
public class OAuthConfiguration extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;

  private final UserDetailsService userService;

  @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
  private String clientSecret;

  @Value("${oauth2.authorizedGrantTypes: implicit, password, refresh_token}")
  private String[] authorizedGrantTypes;

  public OAuthConfiguration(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, @Qualifier("userDetailsServiceImpl") UserDetailsService userService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.inMemory()
        .withClient(clientId)
        .secret(clientSecret)
        .authorizedGrantTypes(authorizedGrantTypes)
        .scopes("user");
  }

  @Override
  public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
//        .accessTokenConverter(accessTokenConverter())
        .userDetailsService(userService)
        .authenticationManager(authenticationManager);
  }

//  @Bean
//  JwtAccessTokenConverter accessTokenConverter() {
//    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//    return converter;
//  }
}
