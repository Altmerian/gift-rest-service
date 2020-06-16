package com.epam.esm.service;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.Precondition;
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
    Optional<Tag> tagOptional = tagRepository.getById(id);
    if (!tagOptional.isPresent()) {
      throw new ResourceNotFoundException("Can't find a tag with id = " + id);
    }
    return convertToDTO(tagOptional.get());
  }

  @Override
  public long create(TagDTO tagDTO) {
    return tagRepository.create(convertToEntity(tagDTO));
  }

  @Override
  public void delete(long id) {
    Tag tag = Precondition.checkExistence(tagRepository.getById(id));
    tagRepository.delete(tag);
  }

  @Override
  public boolean foundDuplicate(String name) {
    return tagRepository.getByName(name).isPresent();
  }

  private TagDTO convertToDTO(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }

  private Tag convertToEntity(TagDTO tagDTO) {
    return modelMapper.map(tagDTO, Tag.class);
  }
}
