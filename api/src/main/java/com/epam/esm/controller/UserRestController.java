package com.epam.esm.controller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.View;
import com.epam.esm.exception.ResourceConflictException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.ParseHelper;
import com.fasterxml.jackson.annotation.JsonView;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller to handle all users related requests. Then requests depending on requests parameters
 * are handled with the appropriate method.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserRestController {

  /** Represents service layer to implement a domain logic and interaction with repository layer. */
  private final UserService userService;

  private final OrderService orderService;
  private final ParseHelper parseHelper;

  @Autowired
  public UserRestController(
      UserService userService, OrderService orderService, ParseHelper parseHelper) {
    this.userService = userService;
    this.orderService = orderService;
    this.parseHelper = parseHelper;
  }

  /**
   * Handles requests that aim to get data about all users in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping
  @JsonView(View.Internal.class)
  public List<UserDTO> getAllUsers(
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    resp.setHeader("X-Total-Count", String.valueOf(userService.countAll()));
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    return userService.getAll(intPage, intSize);
  }

  /**
   * Handles requests to get data about a user with a specific id
   *
   * @param userId user id
   * @return response with payload filled by data of the searched user
   */
  @GetMapping("/{userId:\\d+}")
  @JsonView(View.Internal.class)
  public UserDTO getUserById(@PathVariable long userId) {
    return userService.getById(userId);
  }

  /**
   * Handles requests that aim to get data about all orders of the particular user in the system
   *
   * @return response with body filled by requested data.
   */
  @GetMapping("/{userId:\\d+}/orders")
  @JsonView(View.Public.class)
  public List<OrderDTO> getUserOrders(
      @PathVariable long userId,
      @RequestParam(value = "page", required = false) String page,
      @RequestParam(value = "size", required = false) String size,
      HttpServletResponse resp) {
    resp.setHeader("X-Total-Count", String.valueOf(orderService.countAll(userId)));
    int intPage = parseHelper.parsePage(page);
    int intSize = parseHelper.parseSize(size);
    return orderService.getByUserId(userId, intPage, intSize);
  }

  /**
   * Handles requests to get data about user's order with a specific id
   *
   * @param userId user id
   * @param orderId order id
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{userId:\\d+}/orders/{orderId:\\d+}")
  @JsonView(View.ExtendedPublic.class)
  public OrderDTO getUserOrderById(@PathVariable long userId, @PathVariable long orderId) {
    return orderService.getByUserIdAndOrderId(userId, orderId);
  }

  /**
   * Handles requests to get data about the most widely used tag of a user with the highest cost of
   * all orders
   *
   * @return response with payload filled by data of the searched order
   */
  @GetMapping("/{userId:\\d+}/tags")
  public List<TagDTO> getWidelyUsedTagsOfUser(@PathVariable long userId) {
    return orderService.getWidelyUsedTagsOfUser(userId);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the order to be
   * persisted in the system
   *
   * @param userDTO user data in a certain format for transfer
   * @param resp HTTP response
   * @throws ResourceConflictException if user with given name already exists
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createUser(@Valid @RequestBody UserDTO userDTO, HttpServletResponse resp) {
    long userId = userService.create(userDTO);
    String location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(userId)
            .toUriString();
    resp.setHeader("Location", location);
  }

  /**
   * Handles requests which use POST HTTP method and request body with data of the user's order to
   * be persisted in the system
   *
   * @param userId user id
   * @param orderDTO order data in a certain format for transfer
   * @param req HTTP request
   * @param resp HTTP response
   * @throws ResourceConflictException if order with given name already exists
   */
  @PostMapping("/{userId:\\d+}/orders")
  @ResponseStatus(HttpStatus.CREATED)
  public void createOrder(
      @PathVariable long userId,
      @Valid @RequestBody OrderDTO orderDTO,
      HttpServletRequest req,
      HttpServletResponse resp) {
    long orderId = orderService.create(userId, orderDTO);
    String url = req.getRequestURL().toString();
    resp.setHeader("Location", url + "/" + orderId);
  }

  /**
   * Handles requests which use DELETE HTTP method to delete all data linked with a certain user in
   * the system
   *
   * @param userId user id
   */
  @DeleteMapping("/{userId:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("userId") long userId) {
    userService.delete(userId);
  }

  /**
   * Handles requests which use DELETE HTTP method to delete user's order
   *
   * @param userId user id
   * @param orderId order id
   */
  @DeleteMapping("/{userId:\\d+}/orders/{id:\\d+}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable long userId, @PathVariable("id") long orderId) {
    orderService.delete(userId, orderId);
  }
}
