package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.specification.EmailUserSpecification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    EmailUserSpecification specification = new EmailUserSpecification(email);
    User user =
        userRepository.query(specification).stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        String.format("User with email %s was not found", email)));

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
  }
}
