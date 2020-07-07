package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.specification.UserIdOrderSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final CertificateRepository certificateRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public OrderServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      @Qualifier("certificateJPARepository") CertificateRepository certificateRepository,
      ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.certificateRepository = certificateRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<OrderDTO> getAll(int page, int size) {
    return orderRepository.getAll(page, size).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public List<OrderDTO> getByUserId(long userId, int page, int size) {
    checkUserId(userId);
    UserIdOrderSpecification specification = new UserIdOrderSpecification(userId);
    return orderRepository.query(specification, page, size).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public long countAll() {
    return orderRepository.countAll();
  }

  @Override
  public long countAll(long userId) {
    return orderRepository.countAll(userId);
  }

  @Override
  public OrderDTO getById(long userId, long id) {
    checkUserId(userId);
    Order order = orderRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    return convertToDTO(order);
  }

  @Override
  public long create(long userId, OrderDTO orderDTO) {
    User user = userRepository.get(userId).orElseThrow(() -> new ResourceNotFoundException(userId));
    Order order = convertToEntity(orderDTO);
    order.setUser(user);
    order.setCertificates(fetchCertificatesData(orderDTO.getCertificates()));
    order.setCost(calculateCost(order.getCertificates()));
    return orderRepository.create(order);
  }

  @Override
  public void delete(long userId, long id) {
    checkUserId(userId);
    Order order = orderRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    orderRepository.delete(order);
  }

  private void checkUserId(long userId) {
    userRepository
        .get(userId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("User with id=%d hasn't been found", userId)));
  }

  private List<Certificate> fetchCertificatesData(List<CertificateDTO> certificates) {
    return certificates.stream()
        .map(
            certificate ->
                certificateRepository
                    .get(certificate.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(certificate.getId())))
        .collect(Collectors.toList());
  }

  private BigDecimal calculateCost(List<Certificate> certificates) {
    return certificates.stream()
        .map(Certificate::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  OrderDTO convertToDTO(Order order) {
    return modelMapper.map(order, OrderDTO.class);
  }

  Order convertToEntity(OrderDTO orderDTO) {
    return modelMapper.map(orderDTO, Order.class);
  }
}
