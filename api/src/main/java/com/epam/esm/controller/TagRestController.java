package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle all tags related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("api/v1/tags")
class TagRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final TagService tagService;

  @Autowired
  public TagRestController(TagService tagService) {
    this.tagService = tagService;
  }

  /**
   * Handles requests that aim to get data about all tags in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping("/")
  public List<TagDTO> getAll() {
    return tagService.getAll();
  }

  /**
   * Handles requests to get data about a tag with a specific id
   *
   * @param id tag id
   * @return response with payload filled by data of the searched tag
   */
  @GetMapping("/{id:\\d+}")
  public TagDTO getById(@PathVariable long id) {
    return tagService.getById(id);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the tag to be
   * persisted in the system
   *
   * @param tagDTO tag data in a certain format for transfer
   * @param req HTTP request
   * @param resp HTTP response
   * @throws ResourceConflictException if tag with given name already exists
   */
  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @Valid @RequestBody TagDTO tagDTO, HttpServletRequest req, HttpServletResponse resp)
      throws ResourceConflictException {
    if (tagService.foundDuplicate(tagDTO)) {
      throw new ResourceConflictException(
          "Your data conflicts with existing resources. A Tag with the given name already exists");
    }
    long tagId = tagService.create(tagDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + tagId);
  }

  /**
   * Handles requests which use DELETE HTTP method to delete all data linked with a certain
   * tag in the system
   *
   * @param id certificate id
   */
  @DeleteMapping("/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") long id) {
    tagService.delete(id);
  }
}
