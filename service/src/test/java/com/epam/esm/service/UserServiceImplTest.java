package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("User service")
class UserServiceImplTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private OrderRepository orderRepository;
  @Mock private UserRepository userRepository;
  @Mock private OrderDTO mockOrderDTO;
  @Mock private Order mockOrder;
  @Mock private User mockUser;
  @Mock private UserDTO mockUserDTO;
  @Mock private ModelMapper modelMapper;
  @Mock private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    when(userService.convertToDTO(mockUser)).thenReturn(mockUserDTO);
    when(userService.convertToEntity(mockUserDTO)).thenReturn(mockUser);
  }

  @Test
  void getAll_queryForAll_expectedListOfAllUsers() {
    // given
    when(userRepository.getAll(1, 1)).thenReturn(Collections.singletonList(mockUser));
    // when
    List<UserDTO> userDTOList = userService.getAll(1, 1);
    // then
    verify(userRepository).getAll(1, 1);
    assertThat(userDTOList, hasSize(1));
  }

  @Test
  void countAll_whenCountAll_thenExpectedCount() {
    // given
    when(userRepository.countAll()).thenReturn(2L);
    // when
    long actualCount = userService.countAll();
    // then
    verify(userRepository).countAll();
    assertThat(actualCount, is(equalTo(2L)));
  }

  @Test
  void getById_givenUserId_expectedUserDTO(){
    // given
    when(userRepository.get(anyLong())).thenReturn(Optional.of(mockUser));
    // when
    UserDTO userDTO = userService.getById(1L);
    // then
    verify(userRepository).get(anyLong());
    assertThat(userDTO, is(equalTo(mockUserDTO)));
  }

  @Test
  void getById_nonexistentUserId_thenExceptionThrows() {
    // given
    long nonexistentUserId = 666L;
    when(userRepository.get(nonexistentUserId)).thenReturn(Optional.empty());
    // when
    Executable retrievingAttempt = () -> userService.getById(nonexistentUserId);
    // then
    assertThrows(ResourceNotFoundException.class, retrievingAttempt);
  }

  @Test
  void create_givenUserDTO_expectedPersistedUserId() {
    // given
    when(userRepository.create(any(User.class))).thenReturn(1L);
    when(userRepository.contains(any(User.class))).thenReturn(false);
    when(mockUserDTO.getPassword()).thenReturn("password");
    // when
    long userId = userService.create(mockUserDTO);
    // then
    verify(userRepository).create(any(User.class));
    verify(userRepository).contains(any(User.class));
    assertThat(userId, is(equalTo(1L)));

  }

  @Test
  void delete_givenUserDTOId_shouldInvokeRepositoryDeleteMethod() {
    // given
    long userDTOId = 1L;
    Set<Order> orders = Collections.singleton(mockOrder);
    when(userRepository.get(userDTOId)).thenReturn(Optional.of(mockUser));
    when(mockUser.getOrders()).thenReturn(orders);
    // when
    userService.delete(userDTOId);
    // then
    verify(userRepository).get(userDTOId);
    verify(userRepository).delete(mockUser);
    verify(orderRepository).delete(mockOrder);
  }

  @Test
  void getByEmail_givenUserEmail_expectedUserDTO() {
    // given
    when(userRepository.query(any())).thenReturn(Collections.singletonList(mockUser));
    // when
    UserDTO userDTO = userService.getByEmail(anyString());
    // then
    verify(userRepository).query(any());
    assertThat(userDTO, is(equalTo(mockUserDTO)));
  }

  @Test
  void checkDuplicate_givenExistedUser_expectedException() {
    // given
    when(userRepository.contains(any(User.class))).thenReturn(true);
    // when
    Executable checkingAttempt = () -> userService.checkForDuplicate(mockUserDTO);
    // then
    assertThrows(ResourceConflictException.class, checkingAttempt);
    verify(userRepository).contains(any(User.class));
  }

  @Test
  void checkDuplicate_uniqueUser_expectedNotToThrow() {
    // given
    when(userRepository.contains(any(User.class))).thenReturn(false);
    // when
    Executable checkingAttempt = () -> userService.checkForDuplicate(mockUserDTO);
    // then
    assertDoesNotThrow(checkingAttempt);
    verify(userRepository).contains(any(User.class));
  }
}
