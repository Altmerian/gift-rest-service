package com.epam.esm.service;

import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.security.AppUserDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User service")
class UserDetailsServiceImplTest {

  @InjectMocks private UserDetailsServiceImpl userDetailsService;

  @Mock private UserRepository userRepository;
  @Mock private User mockUser;

  @Test
  void loadUserByUsername_givenUserEmail_expectedAppUserDetails() {
    // given
    User user = new User();
    user.setId(1L);
    user.setPassword("password");
    user.setEmail("email");
    user.setUserRole(UserRole.USER);
    when(userRepository.query(any())).thenReturn(Collections.singletonList(user));
    // when
    AppUserDetails appUserDetails = userDetailsService.loadUserByUsername(user.getEmail());
    // then
    verify(userRepository).query(any());
    assertNotNull(appUserDetails);
  }
}
