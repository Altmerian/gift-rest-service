package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.AppUserDetails;
import com.epam.esm.specification.EmailUserSpecification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public AppUserDetails loadUserByUsername(String email) {
    EmailUserSpecification specification = new EmailUserSpecification(email);
    User user =
        userRepository.query(specification).stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("User with email %s was not found", email)));
    List<SimpleGrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().getAuthority()));
    return new AppUserDetails(user.getEmail(), user.getPassword(), user.getId(), authorities);
  }
}
