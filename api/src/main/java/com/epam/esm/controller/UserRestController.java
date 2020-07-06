package com.epam.esm.controller;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.UserService;
import com.epam.esm.util.ParseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle all users related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("api/v1/users")
class UserRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final UserService userService;

  @Autowired
  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Handles requests that aim to get data about all users in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping("/")
  public List<UserDTO> getAll(
      @RequestParam(value = "page", defaultValue = "1") String page,
      @RequestParam(value = "size", defaultValue = "20") String size,
      HttpServletResponse resp) {
    resp.setHeader("X-Total-Count", String.valueOf(userService.countAll()));
    int intPage = ParseHelper.parsePage(page);
    int intSize = ParseHelper.parseSize(size);
    return userService.getAll(intPage, intSize);
  }

  /**
   * Handles requests to get data about a user with a specific id
   *
   * @param id user id
   * @return response with payload filled by data of the searched user
   */
  @GetMapping("/{id:\\d+}")
  public UserDTO getById(@PathVariable long id) {
    return userService.getById(id);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the user to be
   * persisted in the system
   *
   * @param userDTO user data in a certain format for transfer
   * @param req HTTP request
   * @param resp HTTP response
   * @throws ResourceConflictException if user with given name already exists
   */
  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public void create(
      @Valid @RequestBody UserDTO userDTO, HttpServletRequest req, HttpServletResponse resp) {
    long userId = userService.create(userDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + userId);
  }

  /**
   * Handles requests which use DELETE HTTP method to delete all data linked with a certain user in
   * the system
   *
   * @param id certificate id
   */
  @DeleteMapping("/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") long id) {
    userService.delete(id);
  }
}
