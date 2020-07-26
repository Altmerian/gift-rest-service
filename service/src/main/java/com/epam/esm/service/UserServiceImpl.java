package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.specification.EmailUserSpecification;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      OrderRepository orderRepository,
      ModelMapper modelMapper,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.modelMapper = modelMapper;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public List<UserDTO> getAll(int page, int size) {
    return userRepository.getAll(page, size).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public long countAll() {
    return userRepository.countAll();
  }

  @Override
  public UserDTO getById(long id) {
    User user = userRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    return convertToDTO(user);
  }

  @Override
  public long create(UserDTO userDTO) {
    checkForDuplicate(userDTO);
    User user = convertToEntity(userDTO);
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    return userRepository.create(user);
  }

  @Transactional
  @Override
  public void delete(long id) {
    User user = userRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    user.getOrders().forEach(orderRepository::delete);
    userRepository.delete(user);
  }

  @Override
  public UserDTO getByEmail(String email) {
    EmailUserSpecification specification = new EmailUserSpecification(email);
    return userRepository.query(specification).stream()
        .map(this::convertToDTO)
        .findFirst()
        .orElseThrow(() -> new ResourceNotFoundException("Can't find User with email=" + email));
  }

  @Override
  public void checkForDuplicate(UserDTO userDTO) throws ResourceConflictException {
    if (userRepository.contains(convertToEntity(userDTO))) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. A User with the given email already exists");
    }
  }

  @VisibleForTesting
  UserDTO convertToDTO(User user) {
    return modelMapper.map(user, UserDTO.class);
  }

  @VisibleForTesting
  User convertToEntity(UserDTO userDTO) {
    return modelMapper.map(userDTO, User.class);
  }
}
