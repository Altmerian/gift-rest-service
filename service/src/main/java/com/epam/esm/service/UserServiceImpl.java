package com.epam.esm.service;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
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
    return userRepository.create(convertToEntity(userDTO));
  }

  @Override
  public void delete(long id) {
    User user = userRepository.get(id).orElseThrow(() -> new ResourceNotFoundException(id));
    userRepository.delete(user);
  }

  @Override
  public void checkForDuplicate(UserDTO userDTO) throws ResourceConflictException {
    if (userRepository.contains(convertToEntity(userDTO))) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. A User with the given email already exists");
    }
  }

  UserDTO convertToDTO(User user) {
    return modelMapper.map(user, UserDTO.class);
  }

  @VisibleForTesting
  User convertToEntity(UserDTO userDTO) {
    return modelMapper.map(userDTO, User.class);
  }
}
