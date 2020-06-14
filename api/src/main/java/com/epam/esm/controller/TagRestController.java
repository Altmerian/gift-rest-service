package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("api/tags")
class TagRestController {

  private final TagService tagService;

  @Autowired
  public TagRestController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping
  public List<TagDTO> getAll() {
    return tagService.getAll();
  }

  @GetMapping("/{id}")
  public TagDTO getById(@PathVariable long id) {
    return tagService.getById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @RequestBody TagDTO tagDTO, HttpServletRequest req, HttpServletResponse resp)
      throws ResourceConflictException {
    if (tagService.foundDuplicate(tagDTO.getName())) {
      throw new ResourceConflictException("Your data conflicts with existing resources");
    }
    long tagId = tagService.create(tagDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + tagId);
  }

  @DeleteMapping(value = "/{id}")
  public void delete(@PathVariable("id") long id) {
    tagService.getById(id);
    tagService.delete(id);
  }


}
