package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.google.common.annotations.VisibleForTesting;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
    this.tagRepository = tagRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<TagDTO> getAll() {
    return tagRepository.getAll().stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  @Override
  public TagDTO getById(long id) {
    Optional<Tag> tagOptional = tagRepository.get(id);
    if (!tagOptional.isPresent()) {
      throw new ResourceNotFoundException("Can't find a tag with id = " + id);
    }
    return convertToDTO(tagOptional.get());
  }

  @Override
  public long create(TagDTO tagDTO) {
    checkForDuplicate(tagDTO);
    return tagRepository.create(convertToEntity(tagDTO));
  }

  @Override
  public void delete(long id) {
    Optional<Tag> tagOptional = tagRepository.get(id);
    if (!tagOptional.isPresent()) {
      throw new ResourceNotFoundException(id);
    }
    tagRepository.delete(tagOptional.get());
  }

  @Override
  public void checkForDuplicate(TagDTO tagDTO) {
    if (tagRepository.contains(convertToEntity(tagDTO))) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. A Tag with the given name already exists");
    }
  }

  @VisibleForTesting
  TagDTO convertToDTO(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }

  @VisibleForTesting
  Tag convertToEntity(TagDTO tagDTO) {
    return modelMapper.map(tagDTO, Tag.class);
  }
}
