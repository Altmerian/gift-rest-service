package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.MinorResourceNotFoundException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.specification.UserIdOrderIdSpecification;
import com.epam.esm.specification.UserIdOrderSpecification;
import com.epam.esm.specification.ValuableUserTagsSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final CertificateRepository certificateRepository;
  private final TagRepository tagRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public OrderServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      @Qualifier("certificateJPARepository") CertificateRepository certificateRepository,
      @Qualifier("tagJPARepository") TagRepository tagRepository,
      ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.certificateRepository = certificateRepository;
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<OrderDTO> getAll(int page, int size) {
    return orderRepository.getAll(page, size).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public long countAll() {
    return orderRepository.countAll();
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
  public long countAll(long userId) {
    return orderRepository.countAll(userId);
  }

  @Override
  public OrderDTO getById(long orderId) {
    Order order =
        orderRepository.get(orderId).orElseThrow(() -> new ResourceNotFoundException(orderId));
    return convertToDTO(order);
  }

  @Override
  public OrderDTO getByUserIdAndOrderId(long userId, long orderId) {
    checkUserId(userId);
    UserIdOrderIdSpecification specification = new UserIdOrderIdSpecification(userId, orderId);
    return convertToDTO(
        orderRepository.query(specification, 1, 10).stream()
            .findFirst()
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        String.format("Can't find an order with id=%d", orderId))));
  }

  @Override
  public List<TagDTO> getWidelyUsedTagsOfUser(long userId) {
    checkUserId(userId);
    ValuableUserTagsSpecification specification = new ValuableUserTagsSpecification(userId);
    List<Tag> tags = tagRepository.query(specification);
    return tags.stream().map(this::convertTagToDTO).collect(Collectors.toList());
  }

  @Override
  public long create(long userId, OrderDTO orderDTO) {
    User user = checkUserId(userId);
    Order order = convertToEntity(orderDTO);
    order.setUser(user);
    order.setCertificates(fetchCertificatesData(orderDTO.getCertificates()));
    order.setCost(calculateCost(order.getCertificates()));
    return orderRepository.create(order);
  }

  @Override
  public void delete(long userId, long orderId) {
    checkUserId(userId);
    delete(orderId);
  }

  @Override
  public void delete(long id) {
    Order order = orderRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    orderRepository.delete(order);
  }

  private User checkUserId(long userId) {
    return userRepository
        .get(userId)
        .filter(user -> !user.isDeleted())
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    String.format("User with id=%d hasn't been found", userId)));
  }

  private List<Certificate> fetchCertificatesData(List<CertificateDTO> certificateDTOS) {
    List<Certificate> certificates = new ArrayList<>();
    for (CertificateDTO certificateDTO : certificateDTOS) {
      Certificate certificate =
          certificateRepository
              .get(certificateDTO.getId())
              .filter(cert -> !cert.isDeleted())
              .orElseThrow(() ->new MinorResourceNotFoundException(
                          Certificate.class, certificateDTO.getId()));
      certificates.add(certificate);
    }
    return certificates;
  }

  private BigDecimal calculateCost(List<Certificate> certificates) {
    return certificates.stream()
        .map(Certificate::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private OrderDTO convertToDTO(Order order) {
    return modelMapper.map(order, OrderDTO.class);
  }

  private Order convertToEntity(OrderDTO orderDTO) {
    return modelMapper.map(orderDTO, Order.class);
  }

  private TagDTO convertTagToDTO(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }
}
