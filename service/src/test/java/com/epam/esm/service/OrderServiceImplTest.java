package com.epam.esm.service;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Order service")
class OrderServiceImplTest {

  @InjectMocks private OrderServiceImpl orderService;

  @Mock private OrderRepository orderRepository;
  @Mock private UserRepository userRepository;
  @Mock private TagRepository tagRepository;
  @Mock private ModelMapper modelMapper;
  @Mock private Order mockOrder;
  @Mock private OrderDTO mockOrderDTO;
  @Mock private Tag mockTag;
  @Mock private TagDTO mockTagDTO;
  @Mock private User mockUser;

  @BeforeEach
  void setUp() {
    when(orderService.convertToDTO(mockOrder)).thenReturn(mockOrderDTO);
    when(orderService.convertToEntity(mockOrderDTO)).thenReturn(mockOrder);
    when(orderService.convertTagToDTO(mockTag)).thenReturn(mockTagDTO);
  }

  @Test
  public void getAll_queryForAll_expectedListOfAllOrders() {
    // given
    when(orderRepository.getAll(1, 1)).thenReturn(Collections.singletonList(mockOrder));
    // when
    List<OrderDTO> orderDTOList = orderService.getAll(1, 1);
    // then
    verify(orderRepository).getAll(1, 1);
    assertThat(orderDTOList, hasSize(1));
  }

  @Test
  public void countAll_whenCountAll_thenExpectedCount() {
    // given
    when(orderRepository.countAll()).thenReturn(2L);
    // when
    long actualCount = orderService.countAll();
    // then
    verify(orderRepository).countAll();
    assertThat(actualCount, is(equalTo(2L)));
  }

  @Test
  public void getById_givenOrderId_expectedOrderDTO() {
    // given
    when(orderRepository.get(anyLong())).thenReturn(Optional.of(mockOrder));
    // when
    OrderDTO orderDTO = orderService.getById(1L);
    // then
    verify(orderRepository).get(anyLong());
    assertThat(orderDTO, is(equalTo(mockOrderDTO)));
  }

  @Test
  public void getById_nonexistentOrderId_thenExceptionThrows() {
    // given
    long nonexistentOrderId = 666L;
    when(orderRepository.get(nonexistentOrderId)).thenReturn(Optional.empty());
    // when
    Executable retrievingAttempt = () -> orderService.getById(nonexistentOrderId);
    // then
    assertThrows(ResourceNotFoundException.class, retrievingAttempt);
  }

  @Test
  void getByUserId_givenUserId_expectedListOfOrderDTO() {
    // given
    when(orderRepository.query(any(), anyInt(), anyInt()))
        .thenReturn(Collections.singletonList(mockOrder));
    when(userRepository.get(anyLong())).thenReturn(Optional.of(mockUser));
    // when
    List<OrderDTO> actualOrderDTOList = orderService.getByUserId(1L, 1, 10);
    // then
    verify(orderRepository).query(any(), anyInt(), anyInt());
    verify(userRepository).get(anyLong());
    assertThat(actualOrderDTOList, hasSize(1));
  }

  @Test
  void getByUserIdAndOrderId_givenUserIdOrderId_expectedOrderDTO() {
    // given
    when(orderRepository.query(any(), anyInt(), anyInt()))
        .thenReturn(Collections.singletonList(mockOrder));
    when(userRepository.get(anyLong())).thenReturn(Optional.of(mockUser));
    // when
    OrderDTO orderDTO = orderService.getByUserIdAndOrderId(1L, 1L);
    // then
    verify(orderRepository).query(any(), anyInt(), anyInt());
    verify(userRepository).get(anyLong());
    assertThat(orderDTO, is(equalTo(mockOrderDTO)));
  }

  @Test
  void getWidelyUsedTagsOfUser_givenUserId_expectedTags() {
    // given
    when(tagRepository.query(any())).thenReturn(Collections.singletonList(mockTag));
    when(userRepository.get(anyLong())).thenReturn(Optional.of(mockUser));
    // when
    List<TagDTO> tagDTOList = orderService.getWidelyUsedTagsOfUser(1L);
    // then
    verify(tagRepository).query(any());
    verify(userRepository).get(anyLong());
    assertThat(tagDTOList, hasSize(1));
  }

  @Test
  void create_givenOrderDTO_expectedPersistedOrderId() {
    // given
    when(orderRepository.create(any(Order.class))).thenReturn(1L);
    when(userRepository.get(anyLong())).thenReturn(Optional.of(mockUser));
    // when
    long orderId = orderService.create(2L, mockOrderDTO);
    // then
    verify(orderRepository).create(any(Order.class));
    assertThat(orderId, is(equalTo(1L)));
  }

  @Test
  void delete_givenOrderDTOId_shouldInvokeRepositoryDeleteMethod() {
    // given
    long orderDTOId = 1L;
    when(orderRepository.get(orderDTOId)).thenReturn(Optional.of(mockOrder));
    // when
    orderService.delete(orderDTOId);
    // then
    verify(orderRepository).get(orderDTOId);
    verify(orderRepository).delete(mockOrder);
  }
}
