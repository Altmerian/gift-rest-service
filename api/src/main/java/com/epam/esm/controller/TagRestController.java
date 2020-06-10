package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.TagService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/tags")
class TagRestController {

  private final TagService tagService;
  private final ModelMapper modelMapper;

  @Autowired
  public TagRestController(TagService tagService, ModelMapper modelMapper) {
    this.tagService = tagService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public List<TagDTO> getAll() {
    return tagService.getAll().stream().map(this::convertToDTO).collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public TagDTO getById(@PathVariable long id) {
    return convertToDTO(tagService.getById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @RequestBody TagDTO tagDTO, HttpServletRequest req, HttpServletResponse resp)
      throws ResourceConflictException {
    Tag tag = convertToEntity(tagDTO);
    if (tagService.foundDuplicate(tag.getName())) {
      throw new ResourceConflictException("Your data conflicts with existing resources");
    }
    long tagId = tagService.create(tag);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + tagId);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public boolean delete(@PathVariable("id") long id) {
    return tagService.delete(id);
  }

  private TagDTO convertToDTO(Tag tag) {
    return modelMapper.map(tag, TagDTO.class);
  }

  private Tag convertToEntity(TagDTO tagDTO) {
    return modelMapper.map(tagDTO, Tag.class);
  }
}
