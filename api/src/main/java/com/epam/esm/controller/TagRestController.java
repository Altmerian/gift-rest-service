package com.epam.esm.controller;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.TagListDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.TagService;
import com.epam.esm.util.ModelAssembler;
import com.epam.esm.util.PageParseHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller to handle all tags related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/tags")
@Api(tags = "Resource: Tag", description = "Tags API for all operations with certificate tags", authorizations = @Authorization(""))
public class TagRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final TagService tagService;

  private final PageParseHelper pageParseHelper;

  @Autowired
  public TagRestController(TagService tagService, PageParseHelper pageParseHelper) {
    this.tagService = tagService;
    this.pageParseHelper = pageParseHelper;
  }

  /**
   * Handles requests that aim to get data about all tags in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping
  @ApiOperation(value = "Get all tags", response = TagListDTO.class)
  public TagListDTO getAll(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    long totalCount = tagService.countAll();
    resp.setHeader("X-Total-Count", String.valueOf(totalCount));
    resp.setHeader("Access-Control-Allow-Origin", "*");
    int intPage = pageParseHelper.parsePage(page);
    int intSize = pageParseHelper.parseSize(size);
    List<TagDTO> tags = tagService.getAll(intPage, intSize);
    tags.forEach(tagDTO -> ModelAssembler.addTagSelfLink(tagDTO, resp));
    TagListDTO tagListDTO = new TagListDTO(tags);
    ModelAssembler.addTagListLinks(tagListDTO, resp);
    return tagListDTO;
  }

  /**
   * Handles requests to get data about a tag with a specific id
   *
   * @param id tag id
   * @return response with payload filled by data of the searched tag
   */
  @GetMapping("/{id:\\d+}")
  @ApiOperation(value = "Find tag by id", response = TagDTO.class)
  @ApiResponses(value = {@ApiResponse(code = 404, message = "Tag not found") })
  public TagDTO getById(@PathVariable long id, HttpServletResponse resp) {
    TagDTO tagDTO = tagService.getById(id);
    ModelAssembler.addTagLinks(tagDTO, resp);
    tagDTO.add(linkTo(methodOn(TagRestController.class).getAll("1", "10", resp)).withRel("getAll"));
    return tagDTO;
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the tag to be
   * persisted in the system
   *
   * @param tagDTO tag data in a certain format for transfer
   * @throws ResourceConflictException if tag with given name already exists
   */
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation(value = "Create new tag", authorizations = @Authorization(value = "Bearer"))
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Created"), @ApiResponse(code = 409, message = "Conflict with existing resource") })
  public ResponseEntity<Object> create(@Valid @RequestBody TagDTO tagDTO) {
    long tagId = tagService.create(tagDTO);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(tagId)
            .toUri();
    return ResponseEntity.created(uri).build();
  }

  /**
   * Handles requests which use DELETE HTTP method to delete all data linked with a certain tag in
   * the system
   *
   * @param id certificate id
   */
  @DeleteMapping("/{id:\\d+}")
  @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation(value = "Delete a tag", authorizations = @Authorization(value = "Bearer"))
  @ApiResponses(value = {@ApiResponse(code = 204, message = "No content"),@ApiResponse(code = 404, message = "Tag not found") })
  public ResponseEntity<Object> delete(@PathVariable("id") long id) {
    tagService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
